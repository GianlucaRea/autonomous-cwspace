module.exports = class Energy {

    constructor(settings) {
        this.frequency = settings.frequency;
        this.max = 500
        this.min = 0
        this.timestamp = Date.now();
        this.energy = 0;
        this.socketsInUse = [];
        this.sockets = [];
        this.numberOfSockets = settings.sockets;

        for(let i = 1; i <= this.numberOfSockets; i++) {
            if(i % 2 == 0){
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

    get(){
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: this.energy
            });
        }
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

    // Sets the heating either to true or false, returns a 204 (succesful request with empty response)
    setElectricSocket() {
        return (req, res) => {
            if(req.body.socket > this.numberOfSockets || req.body.socket <= 0) {
                res.status(400).send(JSON.stringify({
                    message: "This socket does not exists"
                }));
                return;
            }
            this.socketsInUse[req.body.socket] = req.body.socketON // 0 or 1
            this.sockets[req.body.socket] = this.sockets[req.body.socket] * this.socketsInUse[req.body.socket]
            res.status(204).send();
        }
    }

    computeEnergy() {
        return () => {
            this.energy = 0;
            for(let i = 1; i <= this.numberOfSockets; i++) {
                this.sockets[i] = (this.sockets[i] * this.socketsInUse[i]) + (Math.floor(Math.random() * 21) - 10);
                if(this.sockets[i] >= 1500){
                    this.sockets[i] = 1500;
                }
                if(this.sockets[i] < 0 ){
                    this.sockets[i] = 0;
                } 
                this.energy += this.sockets[i];
            }
            this.timestamp = Date.now();

        }
    }
}
