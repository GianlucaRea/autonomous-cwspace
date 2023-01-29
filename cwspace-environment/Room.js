const express = require('express');
const Battery = require('./controllers/battery.js');
const Energy = require('./controllers/energy.js');

module.exports = class Room {

    constructor(settings) {
        this.settings = settings;
        this.router = new express.Router();
        this.battery = new Battery(settings.battery);
        if(settings.id > 0){
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
        this.router.get('/home/sensors/' + this.settings.name + '/battery', this.battery.get());         
        this.router.post('/home/sensors/' + this.settings.name + '/battery/setBatteryOutput', this.battery.setBatteryOutput());
        this.router.post('/home/sensors/' + this.settings.name + '/status', this.battery.setStatus());
        if(this.battery.status === 0){
            this.energy.setClosed();
        }

    }   

    attachGridRoutes() {
        this.router.get('/home/sensors/' + this.settings.name + '/battery', this.battery.getGrid());

    }





}