pondLocation = [170, 200];
pondRadius = 100;
Duck = function(){
    this.x = 0;
    this.y = game.world.randomY;
    this.minSpeed = -45;
    this.maxSpeed = 45;
    this.vx = Math.random()*(this.maxSpeed - this.minSpeed+1)-this.minSpeed;
    this.vy = Math.random()*(this.maxSpeed - this.minSpeed+1)-this.minSpeed;

    this.duck = game.add.sprite(this.x,this.y,"duck");
    this.duck.anchor.setTo(0.5, 0.5);
    this.duck.inputEnabled = true;
    this.duck.input.enableDrag(false, true);
    game.physics.enable(this.duck, Phaser.Physics.ARCADE);
    this.duck.body.collideWorldBounds = true;
    this.duck.body.bounce.setTo(1, 1);
    this.duck.body.velocity.x = this.vx;
    this.duck.body.velocity.y = this.vy;
    this.duck.body.immovable = true;
    this.duck.scale.x=0.4;
    this.duck.scale.y=0.4;
    this.duck.score=0;
    var self=this;
    this.duck.touchDown = function(){
        quack.play();
        self.duck.body.velocity.x = 0;
        self.duck.body.velocity.y = 0;
    };
    this.duck.touchUp = function(){
        self.duck.body.velocity.x = self.vx;
        self.duck.body.velocity.y = self.vy;
    };
    this.duck.events.onInputDown.add(self.duck.touchDown, this);
    this.duck.events.onInputUp.add(self.duck.touchUp, this);
};

var w = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
var h = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);

var game = new Phaser.Game(w, h, Phaser.AUTO, '', { preload: preload, create: create, update: update, render: render });
var numOfDucks = 10;
var pond;
var ducks;
var quack;
var score = 0;
var roundScore = 0;
var time = 30;
var scoreText = "Score: " + score;
var timeText = "Time: " + time;
var timer = new Phaser.Timer(game);


function preload() {
    game.load.image('duck', 'img/duck.png');
    game.load.audio('quack', 'audio/quack.wav');
}

function create() {
    setupTextBar();
    pond = game.add.graphics(0, 0);
    game.stage.backgroundColor = "#4C8F00";
    quack = game.add.audio('quack');
    pond.lineStyle(0);
    pond.beginFill(0x8CF2FF, 0.5);
    pond.drawCircle(pondLocation[0], pondLocation[1], pondRadius);

    ducks = [];
    for (var i=0; i<numOfDucks; i++) {
        ducks.push( new Duck() );
    }
}

function update() {
    game.input.onUp.addOnce(duckScore, this);
}

function duckScore() {
    roundScore = 0;
    for(var i=0;i<numOfDucks;i++) {
        roundScore += ducks[i].duck.score;
    }
    roundScore *= 10;
    scoreText.setText("Score: " + roundScore);
    if (roundScore / 10 == numOfDucks) {
        scoreText.setText("You Win!");
        gameEnd();
    }
}

function gameEnd() {
    score += roundScore;
    //game.stage.backgroundColor = "#FAFAFA";
    timer.stop();
}

function setupTextBar() {
    var style = { font: "45px Arial", fill: "yellow", align: "left" };
    scoreText = game.add.text(0, 0, scoreText, style);

    //  Create Countdown Timer
    timer = game.time.create(false);
    timer.loop(1000, updateTimeCounter, this);
    timer.start();
    timeText = game.add.text(w-200, 0, timeText, style);
}

function updateTimeCounter() {
    time--;
    timeText.setText("Time: " + time);
    if (time <= 0) {
        timeText.setText("Time Up!");
        gameEnd();
    }
}

function render(){
    for(var i=0;i<numOfDucks;i++){
        var ducky = ducks[i];
        var x = ducky.duck.position.x;
        var y = ducky.duck.position.y;
        var center_x = pondLocation[0];
        var center_y = pondLocation[1];
        var radius = pondRadius + 20; //duck width added

        var withinPond = (x - center_x)*(x - center_x) + (y - center_y) * (y - center_y);
        if(withinPond  < radius * radius){
            if (withinPond  < (radius - 5) * (radius - 5)){
                ducky.duck.body.velocity.x *= 0;
                ducky.duck.body.velocity.y *= 0;
                ducky.duck.score = 1;
            }
            ducky.duck.body.velocity.x *= -1;
            ducky.duck.body.velocity.y *= -1;
        } else {

        }
    }
}