const API_BASE = '/api';

const parseJwt = (token) => {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        const payload = JSON.parse(jsonPayload);
        console.log("JWT was decoded: " + payload.sub);
        return payload.sub || payload.username;
    } catch (e) {
        console.error("Failed to decode JWT payload:", e);
        return null;
    }
};
const createSOAPRequest = (method, params) => {
    return `<?xml version="1.0" encoding="UTF-8"?>
    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                   xmlns:lab="http://lab3.itmo/">
    <soap:Header/>
    <soap:Body>
        <lab:${method}>
            ${Object.entries(params).map(([key, value]) => 
        `<${key}>${value}</${key}>`).join('')}
        </lab:${method}>
    </soap:Body>
    </soap:Envelope>`;
};

const extractFaultString = (xmlText) => {
    const match = xmlText.match(/<faultstring>([^<]+)<\/faultstring>/);
    return match ? match[1] : 'Неизвестная ошибка сервера';
};


export const authApi = {
    login: async (credentials) => {
        const soapRequest = createSOAPRequest('login', {
            username: credentials.username,
            password: credentials.password
        });
        console.log("SOAP body created: ", soapRequest);

        const response = await fetch(`${API_BASE}/AuthService`, {
            method: 'POST',
            headers: {
                'Content-Type': 'text/xml; charset=utf-8',
                'SOAPAction': 'http://lab3.itmo/AuthService/login'
            },
            body: soapRequest
        });

        const xmlText = await response.text();
        console.log("xmlTxt received: ", xmlText);

        if (!response.ok) {
            throw new Error(extractFaultString(xmlText));
        }
        const match = xmlText.match(/<return>([^<]+)<\/return>/);

        if (match && match[1]) {
            const token = match[1];
            const username = parseJwt(token);

            if (!username) {
                throw new Error('Ошибка парсинга токена: не удалось получить имя пользователя.');
            }

            return {token, username};
        } else {
            throw new Error('Ошибка сервера: не получен токен');
        }
    },

    register: async (credentials) => {
        const soapRequest = createSOAPRequest('register', {
            username: credentials.username,
            password: credentials.password
        });
        console.log("SOAP body created: ", soapRequest);

        const response = await fetch(`${API_BASE}/AuthService`, {
            method: 'POST',
            headers: {
                'Content-Type': 'text/xml; charset=utf-8',
                'SOAPAction': 'http://lab3.itmo/AuthService/register'
            },
            body: soapRequest
        });

        const xmlText = await response.text();
        console.log("xmlTxt received: ", xmlText);

        if (!response.ok) {
            throw new Error(extractFaultString(xmlText));
        }

        const messageMatch = xmlText.match(/<return>([^<]+)<\/return>/);
        if (messageMatch && messageMatch[1]) {
            return {message: messageMatch[1]};
        } else {
            return {message: 'Регистрация успешна'};
        }
    },

    logout: async (token) => {

        if (token) {
            const soapRequest = createSOAPRequest('logout', {
                token: token
            });
            console.log("SOAP body created: ", soapRequest);

            try {
                await fetch(`${API_BASE}/AuthService`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'text/xml; charset=utf-8',
                        'SOAPAction': 'http://lab3.itmo/AuthService/logout'
                    },
                    body: soapRequest
                });
                console.log("Logout successfully!");
            } catch (error) {
                console.error('Logout error:', error);
            }
        }

        return true;
    },
}