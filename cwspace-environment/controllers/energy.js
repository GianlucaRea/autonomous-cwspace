module.exports = class Energy {

    constructor(settings) {
        this.frequency = settings.frequency;
        this.max = 25
        this.min = 0
        this.timestamp = Date.now();
        this.energy = 0;
        this.socketsInUse = [];
        this.sockets = [];
        this.numberOfSockets = settings.sockets;

        for(let i = 1; i <= this.numberOfSockets; i++) {
            let x = generateRandomNumber(0,10);
            if(x <= 3){
                this.sockets[i] = 0;
                this.socketsInUse[i] = 0;
            } else {
                this.sockets[i] = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;
                this.socketsInUse[i] = 1;
            }
            this.energy += this.sockets[i]
        }

        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computeEnergy(), this.frequency);
    }

    getElectricSocketStatus() {
        return (req, res) => {
            if(req.params.socket > this.numberOfSockets || req.params.socket <= 0) {
                res.status(400).send(JSON.stringify({
                    message: "This socket does not exists"
                }));
                return;
            }

            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: this.sockets[req.params.socket] * this.socketsInUse[req.params.socket]
            });
        }
    }

    get(){
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: this.energy,
                sockets: this.numberOfSockets
            });
        }
    }

    setClosed(){
        return () => {
            this.energy = 0;
            for(let i = 1; i <= this.numberOfSockets; i++) {
                    this.sockets[i] = 0;
                    this.socketsInUse[i] = 0;
            }
            this.energy += this.sockets[i];
        }
    }
    
    computeEnergy() {
        return () => {

                let x = generateRandomNumber(0,10);
                if(x <= 3){
                    this.energy += 75;
                    this.energy = Math.trunc(this.energy );
                }
                else if (x >= 6){
                    this.energy -= 50;
                    this.energy = Math.trunc(this.energy);
                } else {
                    this.energy = Math.trunc(this.energy);
                } 
            
                this.timestamp = Date.now();
                if(this.energy < 0 ) this.energy= 0;
        }
    }
    
}

function generateRandomNumber(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
};
