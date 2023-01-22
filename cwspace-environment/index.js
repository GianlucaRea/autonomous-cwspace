const server = require("express")();
const {urlencoded, json} = require("body-parser");
const config = require('config');
const Room = require("./Room.js");
// Assigning request parsing middlewares to server
server.use(urlencoded({ extended: false }));
server.use(json());

let RoomSettings = config.get('rooms');
let rooms = [];
// Creating office rooms and assigning routers to the server
RoomSettings.forEach(RoomSettings => {
    let room = new Room(RoomSettings);
    server.use(room.router);
    rooms.push(room);
});


server.listen(8000, () => {
    console.log("Server listening on port 8000");
})