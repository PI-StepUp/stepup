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

io.on("connection", (socket) => {
    console.log("User connected");
    socket.on("enter_room", (roomName, done) => {
        socket.join(roomName);
        socket.on("send_message", (item) => {
            socket.to(roomName).emit("message", item);
        })
    })
    socket.on("disconnected", () => {
        console.log("User disconnected");
    })
});

server.listen(port, () => {
    console.log(`server listening on port ${port}`);
});