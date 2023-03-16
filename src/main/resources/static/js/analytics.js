// Orders Per  Month Chart

var sellPerMonth = [];
const monthData = document.querySelectorAll(".monthdata");

monthData.forEach(item=>{
	sellPerMonth.push(parseInt(item.value));
});
console.log(sellPerMonth);

const ctx = document.getElementById('yearChart');

new Chart(ctx, {
	type: 'line',
	data: {
		labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
		datasets: [{
			label: 'Orders Placed every Month',
			data: sellPerMonth,
			borderWidth: 2, // Set the line width to 2 pixels
			backgroundColor: '#272c48',
			borderColor: '#272c48',
		}]
	},
	options: {
		scales: {
			y: {
				beginAtZero: true
			}
		},

	}
});



// Veg Non - veg Ratio

// Accessing the data of veg and nonveg Orders Count



var vegCount = document.getElementById("veg").getAttribute("data-message");
var nonVegCount = document.getElementById("nonveg").getAttribute("data-message");

console.log(vegCount);
console.log(nonVegCount);

const ctx2 = document.getElementById('typeChart');

new Chart(ctx2, {
	type: 'doughnut',
	data: {
		labels: ['Veg', 'Non-veg'],
		datasets: [{
			label: 'Type Food Count',
			data: [vegCount, nonVegCount],
			backgroundColor: [
				'green',
				'red'
			],
			borderWidth: 1
		}]
	},
	options: {
		scales: {
			y: {
				beginAtZero: true
			}
		}
	}
});



// Bar data to show food sell in height
var itemNames = [];
var itemOrderCount = [];

const names = document.querySelectorAll(".barnames");
const count = document.querySelectorAll(".barheights");

names.forEach(item =>{
	itemNames.push(item.value);
});

count.forEach(c=>{
	itemOrderCount.push(parseInt(c.value));
});

console.log(itemNames);
console.log(itemOrderCount);







const ctx3 = document.getElementById('foodChart');

new Chart(ctx3, {
	type: 'bar',
	data: {
		labels: itemNames,
		datasets: [{
			color: '#ffeee7',
			label: 'Food items sell',
			data: itemOrderCount,
			borderWidth: 2,
			backgroundColor: '#ffeee7',
			borderColor: '#f76c28',

		}]
	},
	options: {
		scales: {
			x: {
				ticks: {
					color: "#f76c28",
					font: {
						size: 14 // 'size' now within object 'font {}'
					},
				}
			},
			y: {
				ticks: {
					color: "#f76c28",
					font: {
						size: 14 // 'size' now within object 'font {}'
					},
				}
			}
		}
	}
});


