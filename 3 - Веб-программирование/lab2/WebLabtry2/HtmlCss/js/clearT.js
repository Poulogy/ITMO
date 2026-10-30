// Очистка содержимого таблицы
async function clearTable() {
    const tableBody = document.querySelector('#results-table tbody');
    tableBody.innerHTML = ''
    const request = {
        method: "POST",
        headers: {
            "Content-type": "application/json",
        },
        body: JSON.stringify({
            sessionId: sessionStorage.server_session_id,
            type: "Clear"
        })
    };
    const url = '/app/';
    try{
        const response = await fetch(url, request);
        if (!response.ok){
            console.log("Error in getting response for loadStorage")
        }
        console.log("Запрос успешно отправлен.");
        const data = await response.json();
        console.log("Ответ от сервера: ", data);

    }catch(e){
        console.log("Error in getting response for clearing table" + e)
    }

}