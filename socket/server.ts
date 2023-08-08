const express = require('express');
const app = express();
const http = require('http').Server(app);
const cors = require('cors');

app.use(cors());
const io = require('socket.io')(http, {
  cors:{
    origin: "*",
    methods:["GET", "POST"]
  }
});
const PORT = process.env.PORT || 4002;

let users = {};

let socketToRoom = {};

let result = [];
let maxNumCnt = 0;

const maximum = process.env.MAXIMUM || 4;

io.on('connection', socket => {
    socket.on('join_room', data => {
        if (users[data.room]) {
            const length = users[data.room].length;
            if (length === maximum) {
                socket.to(socket.id).emit('room_full');
                return;
            }
            users[data.room].push({id: socket.id, email: data.email, name: data.name});
        } else {
            users[data.room] = [{id: socket.id, email: data.email, name: data.name}];
        }
        socketToRoom[socket.id] = data.room;

        socket.join(data.room);
        console.log(`[${socketToRoom[socket.id]}]: ${socket.id} enter`);

        const usersInThisRoom = users[data.room].filter(user => user.id !== socket.id);

        console.log(usersInThisRoom);

        io.sockets.to(socket.id).emit('all_users', usersInThisRoom);
    });

    socket.on('offer', data => {
        //console.log(data.sdp);
        socket.to(data.offerReceiveID).emit('getOffer', {sdp: data.sdp, offerSendID: data.offerSendID, offerSendEmail: data.offerSendEmail});
    });

    socket.on('answer', data => {
        //console.log(data.sdp);
        socket.to(data.answerReceiveID).emit('getAnswer', {sdp: data.sdp, answerSendID: data.answerSendID});
    });

    socket.on('candidate', data => {
        //console.log(data.candidate);
        socket.to(data.candidateReceiveID).emit('getCandidate', {candidate: data.candidate, candidateSendID: data.candidateSendID});
    });

    socket.on('send_message', (data,roomName) => {
        io.emit('message', data);
    });

    socket.on('playMusic', (data, roomName) => {
        io.emit('startRandomplay', data);
    });

    socket.on('close_randomplay', (nickname, correct, roomName) => {
        result.push({nickname, correct});
        let winner = "";
        let max = 0;
        // 같은 방 구분할 것

        console.log(users);
        for(let i=0; i<Object.keys(users).length; i++){
            if(users[roomName] != undefined){
                maxNumCnt++;
            }
        }
        if(result.length == maxNumCnt){
            for(let i=0; i<result.length; i++){
                if(max <= result[i].correct){
                    max = result[i].correct;
                    winner = result[i].nickname;
                }
            }
            result = [];
            maxNumCnt = 0;

            io.emit("congraturation", roomName, winner);
        }
    });

    socket.on('finish', (roomName) => {
        console.log(roomName);
        io.emit('cntCorrect', roomName);
    })

    socket.on('disconnect', () => {
        console.log(`[${socketToRoom[socket.id]}]: ${socket.id} exit`);
        const roomID = socketToRoom[socket.id];
        let room = users[roomID];
        if (room) {
            room = room.filter(user => user.id !== socket.id);
            users[roomID] = room;
            if (room.length === 0) {
                delete users[roomID];
                return;
            }
        }
        socket.to(roomID).emit('user_exit', {id: socket.id});
        console.log(users);
    })
});

http.listen(PORT, () => {
    console.log(`server running on ${PORT}`);
});