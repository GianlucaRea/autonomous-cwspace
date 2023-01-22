module.exports = class Battery {

    constructor(settings) {
        this.grid = settings.id;
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

    computeBattery() {
        return () => {
            if(this.grid != 1){
            //Battery Level
            if (this.level > 0 && this.level < 100) this.level = this.level - generateRandomNumber(0,3) + generateRandomNumber(0,3);
            if(this.level < 0) this.level = 0;
            if(this.level > 100) this.level = 100;
            //Battery Output
            if (this.output > 0 && this.output < 100) this.output = this.output - generateRandomNumber(0,3) + generateRandomNumber(0,3);
            if(this.output < 0) this.output = 0;
            if(this.output > 100) this.output = 100;
            //Battery Input
            if (this.input > 0 && this.input < 100) this.input = this.input - generateRandomNumber(0,3) + generateRandomNumber(0,3);
            if(this.input < 0) this.input = 0;
            if(this.input > 100) this.input = 100;
            } else {
                console.log("OEX");
                this.input = 5000;
                this.output = 5000;
                this.capacity = 5000;
                this.level = 5000;
            }
        }
    }

  
    }

    function generateRandomNumber(min, max) {
        return Math.floor(Math.random() * (max - min) + min);
    };

