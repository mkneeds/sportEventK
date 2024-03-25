var ctx = document.getElementById('my_canvas1').getContext('2d');
var ctx1 = document.getElementById('my_canvas2').getContext('2d');
var al = 0;
var start = 4.72;
var cw = ctx.canvas.width;
var ch = ctx.canvas.height;
var diff;
function getToken() {
    const cookies = document.cookie.split('; ');
    const tokenCookie = cookies.find(cookie => cookie.startsWith('jwtToken='));
    if (tokenCookie) {
        return tokenCookie.split('=')[1];
    } else {
        return null;
    }
}
function getPopularCategory(callback) {
    var xhr = new XMLHttpRequest();
    var token = getToken();
    xhr.open("POST", "/popular-category", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                var categoryName = response.categoryName;
                var percentage = response.percentage;
                callback(null, categoryName, percentage);
            } else {
                callback("Произошла ошибка при выполнении запроса: " + xhr.status);
            }
        }
    };

    xhr.send();
}

let catName;
let perCat;
function handlePopularCategory(err, categoryName, percentage) {
   catName = categoryName;
   perCat = percentage;
    var catPopularElement = document.getElementById('catPopular');
    if (catPopularElement) {
        catPopularElement.textContent = "Популярная категория: "+catName;
    } else {
        console.error("Элемент с идентификатором catPopular не найден!");
    }
}
getPopularCategory(handlePopularCategory);

function progressSim(){
    diff = ((al / 100) * Math.PI*2*10).toFixed(2);
    ctx.clearRect(0, 0, cw, ch);
    ctx.lineWidth = 5;
    ctx.fillStyle = '#000';
    ctx.font = '18px Dosis';
    ctx.strokeStyle = "rgb(68,138,255)";
    ctx.textAlign = 'center';
    ctx.fillText(al+'%', cw*.5, ch*.5+5, cw);
    ctx.beginPath();
    ctx.arc(72, 72, 60, start, diff/10+start, false);
    ctx.stroke();
    if(al >= perCat){
        clearTimeout(sim);
    }
    al++;
}
var sim = setInterval(progressSim, 30);




var al1 = 0;
var start1 = 4.72;
var cw1 = ctx.canvas.width;
var ch1 = ctx.canvas.height;
var diff1;
async function getTotalSales() {
    try {
        const response = await fetch('/purchase/calculateTotalSales');
        if (!response.ok) {
            throw new Error('Ошибка при получении общей суммы продаж');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Ошибка:', error);
    }
}
async function getTotalSalesPercantages() {
    try {
        const response = await fetch('/purchase/calculatePercantageTotal');
        if (!response.ok) {
            throw new Error('Ошибка при получении общей суммы продаж');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Ошибка:', error);
    }
}

async function updateTotalPurchase() {
    try {
        const totalSales = await getTotalSales();
        document.getElementById("totalPurchase").textContent = "Сумма продаж: " + totalSales + " BYN";
    } catch (error) {
        console.error('Ошибка:', error);
    }
}

updateTotalPurchase();
let totalSalesPercantages;
getTotalSalesPercantages()
    .then(result => {
        totalSalesPercantages = result;
    })
    .catch(error => {
        console.error('Произошла ошибка:', error);
    });

function progressSim1(){

    diff1 = ((al1 / 100) * Math.PI*2*10).toFixed(2);
    ctx1.clearRect(0, 0, cw, ch);
    ctx1.lineWidth = 5;
    ctx1.fillStyle = '#000';
    ctx1.font = '18px Dosis';
    ctx1.strokeStyle = "rgb(68,138,255)";
    ctx1.textAlign = 'center';
    ctx1.fillText(al1+'%', cw1*.5, ch1*.5+5, cw1);
    ctx1.beginPath();
    ctx1.arc(72, 72, 60, start1, diff1/10+start1, false);
    ctx1.stroke();
    if(al1 >= totalSalesPercantages){
        clearTimeout(sim1);
    }
    al1++;
}
var sim1 = setInterval(progressSim1, 50);

//horizontal percentage bar

var hBar = document.getElementById("HourlyBar");
var width = 1;
var id = setInterval(frame, 30);
function frame() {
    if (width >= 70) {
        clearInterval(id);
    } else {
        width++;
        hBar.style.width = width + '%';
    }
}

var mBar = document.getElementById("MonthlyBar");
var widthM = 1;
var idM = setInterval(frameM, 80);
function frameM() {
    if (widthM >= 20) {
        clearInterval(idM);
    } else {
        widthM++;
        mBar.style.width = widthM + '%';
    }
}

var yBar = document.getElementById("YearlyBar");
var widthY = 1;
var idY = setInterval(frameY, 30);
function frameY() {
    if (widthY >= 70) {
        clearInterval(idY);
    } else {
        widthY++;
        yBar.style.width = widthY + '%';
    }
}



//chartjs Barchart

// var dataLChart = {
//   labels: ["January", "February", "March", "April", "May", "June", "July"],
//   datasets: [{
//     label: "My First dataset",
//     fillColor: "rgba(220,220,220,0.2)",
//     strokeColor: "rgba(220,220,220,1)",
//     pointColor: "rgba(220,220,220,1)",
//     pointStrokeColor: "#fff",
//     pointHighlightFill: "#fff",
//     pointHighlightStroke: "rgba(220,220,220,1)",
//     data: [65, 59, 80, 81, 56, 55, 40]
//   }, {
//     label: "My Second dataset",
//     fillColor: "rgba(151,187,205,0.2)",
//     strokeColor: "rgba(151,187,205,1)",
//     pointColor: "rgba(151,187,205,1)",
//     pointStrokeColor: "#fff",
//     pointHighlightFill: "#fff",
//     pointHighlightStroke: "rgba(151,187,205,1)",
//     data: [28, 48, 40, 19, 86, 27, 90]
//   }]
// };

// var LOptions = {
//   bezierCurve: false,
//   animation: true,
//   animationEasing: "easeOutQuart",
//   showScale: false,
//   tooltipEvents: ["mousemove", "touchstart", "touchmove"],
//   tooltipCornerRadius: 3,
//   pointDot : true,
//   pointDotRadius : 4,
//   datasetFill : true,
//   scaleShowLine : true,
//   animationEasing : "easeOutBounce",
//   animateRotate : true,
//   animateScale : true,
// };


// var contextOfChart = document.getElementById("myChart").getContext("2d");

// var mychart = new Chart(contextOfChart).Line(dataLChart, LOptions);


var ctxOfBarChart = document.getElementById("myChart").getContext("2d");
let userSalesData = [];

async function getUserSalesData() {
    try {
        const response = await fetch('/purchase/getUserSales');
        if (!response.ok) {
            throw new Error('Ошибка при получении данных о продажах пользователя');
        }
        const result = await response.json();
        return result;
    } catch (error) {
        console.error('Ошибка:', error);
        throw error;
    }
}

(async () => {
    try {
        userSalesData = await getUserSalesData();
        var mychart = new Chart(ctxOfBarChart, {
            type: 'bar',
            data: {
                labels: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
                datasets: [
                    {
                        label: "Продажи",
                        backgroundColor: [
                            'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)', 'rgba(68,138,255,1)'
                        ],
                        borderWidth: 2,
                        data: userSalesData
                    }
                ]
            },
            options: {
                scales: {
                    xAxes: [
                        {
                            barThickness: 18
                        }
                    ],
                    yAxes: [{
                        barThickness: 5,
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });
    } catch (error) {
        console.error('Произошла ошибка:', error);
    }
})();

//counter1
var cuntr1 = document.getElementsByClassName("counter1");

setInterval(function () {
    for(var i = 0 ; i < cuntr1.length ; i++) {

        cuntr1[i].innerHTML = (parseFloat(parseFloat(counters[i].innerHTML).toFixed(2)) + parseFloat(parseFloat(counters[i].dataset.increment).toFixed(2))).toFixed(2);
    }
}, 1000);