var http = require('http');

var server = http.createServer(function(req, res) {
	res.writeHead(200);
	res.end();
});

var io = require('socket.io').listen(server);

var nbConnectes = 0;

io.on('connection', function (socket) {

	nbConnectes++;
	console.log(nbConnectes);

	socket.roomId = Math.ceil(nbConnectes/2);
	socket.join('room'+socket.roomId);

	socket.on('player', function (obj) {
		if(!Array.isArray(io.sockets.adapter.rooms['room'+socket.roomId].array)){
			io.sockets.adapter.rooms['room'+socket.roomId].array = new Array();
		}
		io.sockets.adapter.rooms['room'+socket.roomId].array.push(obj);
		if(io.sockets.adapter.rooms['room'+socket.roomId].length == 2){
			randomNumber = Math.floor(Math.random() * 10) + 20;
			socket.emit('player', io.sockets.adapter.rooms['room'+socket.roomId].array[0], randomNumber);
			socket.to('room'+socket.roomId).emit('player', io.sockets.adapter.rooms['room'+socket.roomId].array[1], randomNumber - 1);
		}
	});

	socket.on('lunchMissile', function (coordinates) {
		socket.to('room'+socket.roomId).emit('lunchMissile', coordinates);
	});

	socket.on('next', function () {
		socket.to('room'+socket.roomId).emit('next');
	});

	socket.on('disconnect', function () {
		socket.to('room'+socket.roomId).emit('deco');
		nbConnectes--;
		console.log(nbConnectes);
	});
});

server.listen(8080);