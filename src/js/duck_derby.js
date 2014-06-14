pondLocation = [190, 200];
pondRadius = 90;
Duck = function(){
    this.x = game.world.randomX;
    this.y = game.world.randomY;
    this.minSpeed = -duckVelocity;
    this.maxSpeed = duckVelocity;
    this.vx = Math.random()*(this.maxSpeed - this.minSpeed+1)-this.minSpeed;
    this.vy = Math.random()*(this.maxSpeed - this.minSpeed+1)-this.minSpeed;

    this.duck = game.add.sprite(this.x,this.y,"duck");
    this.duck.anchor.setTo(0.5, 0.5);
    this.duck.inputEnabled = true;
    this.duck.input.enableDrag(false, true);
    game.physics.enable(this.duck, Phaser.Physics.ARCADE);
    this.duck.body.collideWorldBounds = true;
    this.duck.body.bounce.setTo(1, 1.05);
    this.duck.body.velocity.x = this.vx;
    this.duck.body.velocity.y = this.vy;
    this.duck.body.immovable = true;
    this.duck.scale.x=duckScale;
    this.duck.scale.y=duckScale;
    this.duck.scale.x = (Math.random()<.5) ? this.duck.scale.x : this.duck.scale.x * -1;
    this.duck.score=0;
    var self=this;
    this.duck.touchDown = function(){
        quack.play();
        self.duck.body.velocity.x = 0;
        self.duck.body.velocity.y = 0;
        self.duck.scale.x = duckScalePickedUp;
        self.duck.scale.y = duckScalePickedUp;
    };
    this.duck.touchUp = function(){
        self.duck.body.velocity.x = self.vx;
        self.duck.body.velocity.y = self.vy;
        self.duck.scale.x = duckScale;
        self.duck.scale.y = duckScale;
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
var newTotal;
var score = 0;
var roundScore = 0;
var time = 3;
var continueGame = true;
var duckVelocity = 45;
var duckScale = 0.4;
var duckScalePickedUp = duckScale * 1.5;
var objects = 0;
var scoreText = "Score: " + score;
var timeText = "Time: " + time;
var timer = new Phaser.Timer(game);
var continueButton;
var againButton;

var level;
if (localStorage.getItem("gameLevel")) {
    level = parseInt(localStorage.getItem("gameLevel"))
} else {
    level = 1;
}
//{
//    1: {numOfDucks: 10, duckVelocity: 45, time: 30, objects: 0, duckScale: 0.4, pondRadius: 100},
//    2: {numOfDucks: 15, duckVelocity: 45, time: 30, objects: 0, duckScale: 0.4, pondRadius: 100}
//}; //levels aren't implemented yet

function preload() {
    game.load.image('duck', 'img/duck.png');
    game.load.audio('quack', 'audio/quack.wav');
    continueButton = game.load.image('continueButton','img/continue.png');
    againButton = game.load.image('playAgainButton','img/playAgain.png');
}

function create() {
    setupTextBar();
    setupLevel();
    initPond();
    game.stage.backgroundColor = "#62B51F";
    quack = game.add.audio('quack');
    ducks = [];
    for (var i=0; i<numOfDucks; i++) {
        ducks.push( new Duck() );
    }
}

function initPond()
{
    pond = game.add.graphics(0, 0);
    pond.lineStyle(0);
    pond.beginFill(0x19C5FF, 0.65);

    var pondXMax = game.world.bounds.width - pondRadius;
    var pondYMax = game.world.bounds.height - pondRadius;
    pondLocation[0] = Math.floor(Math.random() * pondXMax) + 1 + (pondRadius);
    pondLocation[1] = Math.floor(Math.random() * pondYMax) + 1 + (pondRadius);

    pond.drawCircle(pondLocation[0], pondLocation[1], pondRadius);
}

function update() {
    game.input.onUp.addOnce(duckScore, this);
}

function render(){
    for(var i=0;i<numOfDucks;i++){
        var ducky = ducks[i];
        var x = ducky.duck.position.x;
        var y = ducky.duck.position.y;
        var center_x = pondLocation[0];
        var center_y = pondLocation[1];
        var radius = pondRadius + 20; //duck width added

        // flip the duck 25% of the time
        if (Math.random()<.015) ducky.duck.scale.x *= -1;

        var duckWithinPond = (x - center_x)*(x - center_x) + (y - center_y) * (y - center_y);
        if(duckWithinPond < (radius + 3) * (radius + 3)){
            if (duckWithinPond < (radius - 5) * (radius - 5)){
                //keeps ducks in the pond when they are placed in there, increasing the score
                ducky.duck.body.velocity.x *= 0;
                ducky.duck.body.velocity.y *= 0;
                ducky.duck.score = 1;
            }
            //keeps ducks out of the pond while they waddle around
            ducky.duck.body.velocity.x *= -1;
            ducky.duck.body.velocity.y *= -1;
        } else {

        }
    }
}

function setupTextBar() {
    var style = { font: "25px Arial", fill: "yellow", align: "left" };
    scoreText = game.add.text(0, 0, "Score: " + score, style);

    //  Create Countdown Timer
    timer = game.time.create(false);
    timer.loop(1000, updateTimeCounter, this);
    timer.start();
    timeText = game.add.text(w - 110, 0, timeText, style); //sets the time 100px from the right of the edge of the screen
}

function setupLevel() {
    numOfDucks = level;
}

function updateTimeCounter() {
    time--;
    timeText.setText("Time: " + time);
    if (time <= 0) {
        timeText.setText("Time Up!");
        continueGame = false;
        gameEnd();
    }
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
// This function updates the localstorage to the current best score
function updateBestScore() {
	if(typeof(Storage) !== "undefined") {
		var prevBestScore = parseInt(localStorage.getItem("duckDerbyBestScore"));
	
		//check if the bestScore variable is available in 	
		if(!prevBestScore) {
			prevBestScore = 0;
			localStorage.setItem("duckDerbyBestScore",0);
			
		}
		
		if(score > prevBestScore)
		{
			localStorage.setItem("duckDerbyBestScore", (score));
		}
	} else {
		// Sorry! No local Storage support..
		alert('this version of local web browser does not support local storage');
	}	
}

function gameEnd() {
    timer.stop(true);
    game.isRunning = false;
    for (var i=0; i<numOfDucks; i++) {
        ducks[i].duck.body.velocity.x *= 0;
        ducks[i].duck.body.velocity.y *= 0;
        ducks[i].duck.inputEnabled = false;
    }
    //adjusting the final scores
    score += roundScore;
	// This function updates the best score after each run of the game.
	updateBestScore();
    scoreText.setText("Total Score: " + score);
    newTotal = score + parseInt(localStorage.getItem("totalScore")) || score;
    localStorage.setItem("totalScore", (newTotal));
    roundScore = 0;
    var style = { font: "15px Arial", fill: "yellow", align: "center" };
    var endText;
    if (continueGame) {
        endText = "Congratulations! Your ducks are safe.";
        game.add.button(game.world.centerX - (continueButton.width / 2), 400, 'continueButton', reload, this, 2, 1, 0);
    } else {
        endText = "You finished with a total score of " + newTotal + ".";
        game.add.button(game.world.centerX - (againButton.width / 2), 400, 'playAgainButton', finalView, this, 2, 1, 0);
        scoreText.setText("High Score :" + parseInt(localStorage.getItem("duckDerbyBestScore")));
    }
    game.add.text(0, game.world.centerY-100, endText, style);

}

function reload() {
    localStorage.setItem("gameLevel", (level + 1));
    location.reload();
}

function finalView() {
    localStorage.removeItem("totalScore");
    localStorage.removeItem("gameLevel");
    location.reload(true);
}
