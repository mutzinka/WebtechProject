// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

// Pie Chart Example

const api_url =
    "http://localhost:8080/fishing/api/fishTypes";
async function getResponse() {
    const response = await fetch(
        api_url,
        {
            method: 'GET',
        }
    );
    const data = await response.json(); // Extracting data as a JSON Object from the response

    var ctx = document.getElementById("myPieChart");
    var myPieChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ["Thompson", "Makayabu", "Sambaza", "Tilapia"],
            datasets: [{

                data: [data[0].fishWeight, data[1].fishWeight, data[2].fishWeight,data[3].fishWeight],
                backgroundColor: ['#007bff', '#dc3545', '#ffc107', '#28a745'],
            }],
        },
    });

}

getResponse();





