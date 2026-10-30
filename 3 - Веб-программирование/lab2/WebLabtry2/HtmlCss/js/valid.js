/*метод валидации у*/
function validateY() {
    const y = document.getElementById('y');
    const value = parseFloat(y.value);

    if (isNaN(value) || value < -5 || value > 5) {
        y.setCustomValidity('Y должен быть числом от -5 до 5');
        return false;
    } else {
        y.setCustomValidity('');
        return true;
    }
}
/*метод валидации х и r*/
function validateXR() {
    const xElement = document.querySelector('input[name="x"]:checked');
    const rElement = document.querySelector('input[name="r"]:checked');
    let isValid = true;
    const r = parseFloat(rElement.value);
    const x = parseFloat(xElement.value);
    if (isNaN(r) || r < 1 || r > 3) {
        rElement.setCustomValidity('R должен быть числом от 1 до 3');
        isValid = false;
    }
    if (isNaN(x) || x > 5 || x < -3) {
        xElement.setCustomValidity('X должен быть числом от -3 до 5');
    }
    return isValid;
}
/*метод валидации всех значений*/
function validate(){
    const valXR = validateXR();
    const valY = validateY();
    return (valY && valXR);
}
