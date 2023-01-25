module.exports = class Battery {

    constructor(settings) {
        this.capacity = settings.capacity;
        this.level = settings.level;
        this.input = settings.input;
        this.frequency = settings.frequency;
        this.output = settings.output;
        this.timestamp = Date.now();
        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computeBattery(), this.frequency);
    }

    get() {
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                level: this.level,
                output: this.output,
                input:this.input
            });
        }
    }

    getGrid() {
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                level: null,
                output: 5000,
                input: 5000,
                energyDemand: 0
            });
        }
    }

    computeBattery() {
        return () => {
            //Battery Level
            if (this.level > 0 && this.level < 100) this.level = this.level - generateRandomNumber(0,3) + generateRandomNumber(0,3);
            if(this.level < 0) this.level = 0;
            if(this.level > 100) this.level = 100;
            //Battery Output
            if (this.output > 0 && this.output < 100) this.output = this.output - generateRandomNumber(0,3) + generateRandomNumber(0,3);
            if(this.output < 0) this.output = 0;
            //Battery Input
            if (this.input > 0 && this.input < 100) this.input = this.input - generateRandomNumber(0,3) + generateRandomNumber(0,3);
            if(this.input < 0) this.input = 0;
            this.timestamp = Date.now();
        }
    }

  
    }

    function generateRandomNumber(min, max) {
        return Math.floor(Math.random() * (max - min) + min);
    };

