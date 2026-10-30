
/*метод отправки данных точек на сервер и получение ответа*/

async function update() {
    try {
        const xVal = parseFloat(document.querySelector('input[name="x"]:checked').value);
        const yVal = parseFloat(document.getElementById("y").value.trim().replace(",", "."));
        const rVal = parseFloat(document.querySelector('input[name="r"]:checked').value);

        const valid = validate();

        console.log("Отправка данных:");
        console.log("X:", xVal);
        console.log("Y:", yVal);
        console.log("R:", rVal);
        console.log("Валидные данные:", valid);

        if (valid) {
            const request = {
                method: "POST",
                headers: {
                    "Content-type": "application/json",
                },
                body: JSON.stringify({
                    sessionId: sessionStorage.server_session_id,
                    type: "Update",
                    x: xVal,
                    y: yVal,
                    r: rVal
                })
            };

            const url = '/app/';

            try {
                const response = await fetch(url, request);
                if (!response.ok) {
                    console.log(err + 'Произошла ошибка при отправке данных на сервер');
                }
                const data = await response.json();

                console.log("Запрос успешно отправлен.");
                try{
                    addResultToTable(data)
                    console.log("Ответ от сервера:", data);
                }catch (e){
                    console.log("Ответ от сервера:", data);
                }
            } catch (err) {
                console.log(err + 'Произошла ошибка при отправке данных на сервер');
            }
        }
    } catch (err){
        console.log(err + 'Произошла ошибка функции отправки и валидации');
    }
}


