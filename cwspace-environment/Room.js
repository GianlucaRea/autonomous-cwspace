const express = require('express');
const Battery = require('./controllers/battery.js');
const Energy = require('./controllers/energy.js');

module.exports = class Room {

    constructor(settings) {
        this.settings = settings;
        this.router = new express.Router();
        this.battery = new Battery(settings.battery);

        if(settings.type === "room"){
            this.energy = new Energy(settings.energy);
            this.attachRoomRoutes();

        } else {
            this.attachGridRoutes();
        }
    }

    attachRoomRoutes() {
        // Energy 
        this.router.get('/home/sensors/' + this.settings.name + '/energy', this.energy.get());
        this.router.get('/home/sensors/' + this.settings.name + '/energy/:socket', this.energy.getElectricSocketStatus());
        this.router.post('/home/sensors/' + this.settings.name + '/energy/:socket', this.energy.setElectricSocket());
        this.router.get('/home/sensors/' + this.settings.name + '/battery', this.battery.get());         
    }   

    attachGridRoutes() {
        this.router.get('/home/sensors/' + this.settings.name + '/battey', this.battery.get());
    }



}