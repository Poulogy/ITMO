document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM загружен, можно сразу загрузить данные");
    loadStorage()
});
/*метод загрузки данных*/
async function loadStorage(){
    const request = {
        method: "POST",
        headers: {
            "Content-type": "application/json",
        },
        body: JSON.stringify({
            sessionId: sessionStorage.server_session_id,
            type: "Load"
        })
    };
    const url = '/app/';
    try{
        const response = await fetch(url, request);
        if (!response.ok){
            console.log("Error in getting response for loadStorage")
        }
        const data = await response.json();
        sessionStorage.setItem('server_session_id', data.sessionId);
        console.log("Запрос успешно отправлен.");
        try{
            console.log("Ответ от сервера:", data);
            data.forEach(addResultToTable)
        }catch (e){
            console.log("На этой сессии данных нет, таблица пустая");
        }
    }catch(e){
        console.log("Error in getting response for loading table" + e)
    }

}

/*метод изменения таблицы*/

function addResultToTable(data) {
    try{
        const r = data.r;
        const x = data.x;
        const y = data.y;
        const time = data.currentTime;
        const hit = data.hit;
        const executionTime = data.executionTime;
        const tableBody = document.querySelector('#results-table tbody');
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
                <td>${r}</td>
                <td>${x}</td>
                <td>${y}</td>
                <td>${time}</td>
                <td class="${hit ? 'hit' : 'miss'}">${hit ? 'Попадание' : 'Промах'}</td>
                <td>${executionTime}</td>
            `;
        tableBody.prepend(newRow);
    }catch(err){
        console.log('Произошла ошибка при отправке данных на сервер' + err);
    }
}