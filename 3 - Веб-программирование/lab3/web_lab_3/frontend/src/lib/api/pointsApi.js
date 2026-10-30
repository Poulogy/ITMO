const API_BASE = '/api';

const CreateSOAPRequest = (method, params) => {
    return `<?xml version="1.0" encoding="UTF-8"?>
    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                   xmlns:lab="http://lab3.itmo/">  
        <soap:Header/>
        <soap:Body>
            <lab:${method}>
                ${Object.entries(params)
        .map(([key, value]) => `<${key}>${value}</${key}>`)
        .join('')}
            </lab:${method}>
        </soap:Body> 
    </soap:Envelope>`;
}
const extractFaultString = (xmlText) => {
    const match = xmlText.match(/<faultstring>([^<]+)<\/faultstring>/);
    return match ? match[1] : 'Неизвестная ошибка сервера';
};

export const pointsApi = {
    getPoints: async (token) =>{
        if (!token) {
            throw new Error('Not logged in (No token provided)');
        }

        const soapRequest = CreateSOAPRequest('getPoints', {token: token});
        console.log("SOAP body created: ", soapRequest);

        const Response = await fetch(`${API_BASE}/PointService`, {
            method: 'POST',
            headers: {
                'Content-Type': 'text/xml; charset=utf-8',
                'SOAPAction': 'http://lab3.itmo/PointService/getPoints',
            },
            body: soapRequest
        });

        const xmlText = await Response.text();
        console.log("xmlTxt received: ", xmlText);

        if (!Response.ok) {
            throw new Error(extractFaultString(xmlText));
        }
        try{
            const points = [];
            const pointMatches = xmlText.match(/<return>(.*?)<\/return>/gs);

            if (pointMatches) {
                pointMatches.forEach((match) => {
                    const xMatch = match.match(/<x>([^<]+)<\/x>/);
                    const yMatch = match.match(/<y>([^<]+)<\/y>/);
                    const rMatch = match.match(/<r>([^<]+)<\/r>/);
                    const hitMatch = match.match(/<hit>([^<]+)<\/hit>/);
                    const scriptTimeMatch = match.match(/<scriptTime>([^<]+)<\/scriptTime>/);
                    const serverTimeMatch = match.match(/<serverTime>([^<]+)<\/serverTime>/);

                    if (xMatch && yMatch && rMatch && hitMatch) {
                        points.push({
                            x: parseFloat(xMatch[1]),
                            y: parseFloat(yMatch[1]),
                            r: parseFloat(rMatch[1]),
                            hit: hitMatch[1] === 'true', // Булево значение
                            serverTime: serverTimeMatch ? serverTimeMatch[1] : new Date().toISOString(),
                            scriptTime: scriptTimeMatch ? parseInt(scriptTimeMatch[1]) : 0,
                        })
                    }
                    console.log("Points was extracted: ", points);
                })
            }
            return points;
        }catch (error) {
            console.error('Error parsing points XML:', error);
            return [];
        }
    },
    addPoint: async (point, token) => { // <-- ПРИНИМАЕМ TOKEN
        if (!token) {
            throw new Error('Отсутствует Token.');
        }

        const soapRequest = CreateSOAPRequest('addPoint', {
            x: point.x,
            y: point.y,
            r: point.r,
            token: token,
        });
        console.log("SOAP body created: ", soapRequest);

        const Response = await fetch(`${API_BASE}/PointService`, {
            method: 'POST',
            headers: {
                'Content-Type': 'text/xml; charset=utf-8',
                'SOAPAction': 'http://lab3.itmo/PointService/addPoint',
            },
            body: soapRequest
        });

        const xmlText = await Response.text();
        console.log("xmlTxt received: ", xmlText);

        if (!Response.ok) {
            throw new Error(extractFaultString(xmlText));
        }

        try {
            const xMatch = xmlText.match(/<x>([^<]+)<\/x>/);
            const yMatch = xmlText.match(/<y>([^<]+)<\/y>/);
            const rMatch = xmlText.match(/<r>([^<]+)<\/r>/);
            const hitMatch = xmlText.match(/<hit>([^<]+)<\/hit>/);
            const scriptTimeMatch = xmlText.match(/<scriptTime>([^<]+)<\/scriptTime>/);

            if (xMatch && yMatch && rMatch && hitMatch) {
                return {
                    id: Date.now().toString(),
                    x: parseFloat(xMatch[1]),
                    y: parseFloat(yMatch[1]),
                    r: parseFloat(rMatch[1]),
                    hit: hitMatch[1] === 'true',
                    serverTime: new Date().toISOString(),
                    scriptTime: scriptTimeMatch ? parseInt(scriptTimeMatch[1]) : 0
                };
            }
            console.log("Point was extracted: ", xMatch[1], yMatch[1], rMatch[1], hitMatch[1], parseInt(scriptTimeMatch[1]) );
        } catch (error) {
            console.error('Error parsing point response:', error);
        }

        throw new Error("Не удалось разобрать ответ сервера о точке.");
    }
};