const express = require('express');
const http = require('http');
const socketIO = require('socket.io');
const cors = require('cors');

const port = 4002;

const app = express();
app.use(cors());

const server = http.createServer(app);
const io = socketIO(server, {
    cors: {
        origin: "*",
        methods: ["GET", "POST"]
    }
});

let users = {};
let socketRoom = {};
const MAXIMUM = 6;

io.on("connection", (socket) => {
    console.log(socket.id, "connection");
    socket.on("join_room", (data) => {
      // 방이 기존에 생성되어 있다면
      if (users[data.room]) {
        // 현재 입장하려는 방에 있는 인원수
        const currentRoomLength = users[data.room].length;
        if (currentRoomLength === MAXIMUM) {
          // 인원수가 꽉 찼다면 돌아갑니다.
          socket.to(socket.id).emit("room_full");
          return;
        }
  
        // 여분의 자리가 있다면 해당 방 배열에 추가해줍니다.
        users[data.room] = [...users[data.room], { id: socket.id }];
      } else {
        // 방이 존재하지 않다면 값을 생성하고 추가해줍시다.
        users[data.room] = [{ id: socket.id }];
      }
      socketRoom[socket.id] = data.room;
  
      // 입장
      socket.join(data.room);

      
  
      // 입장하기 전 해당 방의 다른 유저들이 있는지 확인하고
      // 다른 유저가 있었다면 offer-answer을 위해 알려줍니다.
      const others = users[data.room].filter((user) => user.id !== socket.id);
      if (others.length) {
        io.sockets.to(socket.id).emit("all_users", others);
      }
    });

    socket.on("send_message", (data) => {
      socket.to(data.room).emit("message", data);
    })

  
    socket.on("offer", (sdp, roomName) => {
      // offer를 전달받고 다른 유저들에게 전달해 줍니다.
      socket.to(roomName).emit("getOffer", sdp);
    });
  
    socket.on("answer", (sdp, roomName) => {
      // answer를 전달받고 방의 다른 유저들에게 전달해 줍니다.
      socket.to(roomName).emit("getAnswer", sdp);
    });
  
    socket.on("candidate", (candidate, array) => {
      // candidate를 전달받고 방의 다른 유저들에게 전달해 줍니다.
      socket.to(array[0]).emit("getCandidate", candidate, array[1]);
    });
  
    socket.on("disconnect", () => {
      // 방을 나가게 된다면 socketRoom과 users의 정보에서 해당 유저를 지워줍니다.
      const roomID = socketRoom[socket.id];
  
      if (users[roomID]) {
        users[roomID] = users[roomID].filter((user) => user.id !== socket.id);
        if (users[roomID].length === 0) {
          delete users[roomID];
          return;
        }
      }
      delete socketRoom[socket.id];
      socket.broadcast.to(users[roomID]).emit("user_exit", { id: socket.id });
    });
  });

server.listen(port, () => {
    console.log(`server listening on port ${port}`);
});