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
    this.duck.scale.x=duckScale;
    this.duck.scale.y=duckScale;
    this.duck.scale.x = (Math.random()<.5) ? this.duck.scale.x : this.duck.scale.x * -1;
    this.duck.score=0;
    game.physics.enable(this.duck, Phaser.Physics.ARCADE);
    this.duck.body.immovable = false;
    this.duck.body.collideWorldBounds = true;
    this.duck.body.bounce.setTo(1, 1.01);
    this.duck.body.velocity.x = this.vx;
    this.duck.body.velocity.y = this.vy;

    var self = this;
    this.duck.touchDown = function(){
        if (soundOn) {
            quack.play();
        }
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
var pondLocation = [190, 200];
var pondRadius = 90;
var numOfDucks = 10;
var pond;
var ducks;
var quack;
var roundScore = 0;
var continueGame = true;
var duckVelocity = 45;
var duckScale = 0.4;
var duckScalePickedUp = duckScale * 1.5;
var objects = 0;
var time = 20;
var timeText = "Time: " + time;
var timer = new Phaser.Timer(game);
var level = parseInt(localStorage.getItem("gameLevel")) ||  1;
var totalScore = parseInt(localStorage.getItem("totalScore")) || 0;

var soundOn = localStorage.getItem("duckDerbySoundOn") == 'true';
var topLeftText = "Level " + level + " Score: " + roundScore;
var recordScore = false;
var facts = ["YESS stands for Youth Emergency Services and Shelter.",
    "YESS helps children from newborn to age 17.",
    "YESS provides emergency shelter, crisis intervention and counseling.",
    "Adopt a duck.  Help a child.",
    "YESS helps children whose home is not always a safe option.",
    "YESS is open 24 hours a day, 7 days a week, 365 days a year."];
var grass;
function preload() {
    game.load.image('duck', 'img/duck.png');
    game.load.image('soundOn', 'img/sound.png');
    game.load.image('grass', 'img/grass.png');
    game.load.audio('quack', 'audio/quack.wav');
}

function create() {
    setupLevel();
    setupTopBar();
    initPond();
    game.stage.backgroundColor = "#62B51F";
    //  Our tiled scrolling background
    grass = game.add.tileSprite(0, 0, w, h, 'grass');
    game.world.bringToTop(pond);
    game.world.bringToTop(timeText);
    game.world.bringToTop(topLeftText);
    quack = game.add.audio('quack');
    ducks = [];
    for (var i = 0; i < numOfDucks;i++) {
	    var newDuck = new Duck();
		//if duck is created within the pond then keep changing the position till the duck is not in the pond anymore.
 		while(isDuckWithinPond(newDuck)) {
			newDuck.duck.position.x = game.world.randomX;
			newDuck.duck.position.y = game.world.randomY;

		}
        	ducks.push(newDuck);
       }

}

function isDuckWithinPond(newDuck) {
        var x = newDuck.duck.position.x;
        var y = newDuck.duck.position.y;
        var center_x = pondLocation[0];
        var center_y = pondLocation[1];
        var groupRadius = pondRadius + 20; //duck width added


		var distanceFromPond = (x - center_x)*(x - center_x) + (y - center_y) * (y - center_y);
		if(distanceFromPond < (groupRadius+5)*(groupRadius+5)) {
			return true;
		}
		else { return false; }
}

function update() {
    game.input.onUp.addOnce(duckScore, this);
    for(var j=0;j<numOfDucks;j++) {
        for (var k = j + 1; k < numOfDucks; k++) {
            game.physics.arcade.collide(ducks[j].duck, ducks[k].duck, collisionHandler, null, this);
        }
    }
}

function render() {
    for(var i=0;i<numOfDucks;i++){
        var ducky = ducks[i];
        var x = ducky.duck.position.x;
        var y = ducky.duck.position.y;
        var center_x = pondLocation[0];
        var center_y = pondLocation[1];
        var radius = pondRadius + 20; //duck width added

        // flip the duck 25% of the time
        if (Math.random()<.015 && game.isRunning)
            ducky.duck.scale.x *= -1;

        var duckWithinPond = (x - center_x)*(x - center_x) + (y - center_y) * (y - center_y);
        if(duckWithinPond < (radius + 5) * (radius + 5)){
            if (duckWithinPond < (radius - 5) * (radius - 5)){
                //keeps ducks in the pond when they are placed in there, increasing the score
                ducky.duck.body.velocity.x *= 0;
                ducky.duck.body.velocity.y *= 0;
                ducky.duck.score = 1;
            }
            //keeps ducks out of the pond while they waddle around
            ducky.duck.body.velocity.x *= -1;
            ducky.duck.body.velocity.y *= -1;
        }
    }
}

function setupTopBar() {
    var soundButton = game.add.button(0, 0, "soundOn", toggleSound, this, 2, 1, 0);
    soundButton.scale.setTo(0.4, 0.4);
    var style = { font: "25px Arial", fill: "yellow", align: "left" };
    topLeftText = game.add.text(soundButton.width, 0, topLeftText, style);

    //  Create Countdown Timer
    timer = game.time.create(false);
    timer.loop(1000, updateTimeCounter, this);
    timer.start();
    timeText = game.add.text(w - 110, 0, timeText, style); //sets the time 100px from the right of the edge of the screen
}

function setupLevel() {
    numOfDucks = level;
    duckVelocity = 45 + (level * 3);
    pondRadius = 100 - (level * 3);
    time = 20 - level;
    if (level > 10) {
        time = 10;
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
    if (roundScore / 10 == numOfDucks) {
        gameEnd();
    } else {
        topLeftText.setText("Level " + level + " Score: " + roundScore);
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
    totalScore += roundScore;
    topLeftText.setText("Total Score: " + totalScore);
    localStorage.setItem("totalScore", totalScore);
    // This function updates the best score after each run of the game.
    updateBestScore();
    roundScore = 0;
    if (continueGame) {
        showOverlay("continue");
    } else {
        showOverlay("playagain");
        topLeftText.setText("High Score : " + parseInt(localStorage.getItem("duckDerbyBestScore")));
    }
}

// This function updates the localstorage to the current best score
function updateBestScore() {
    if(typeof(Storage) !== "undefined") {
        var prevBestScore = parseInt(localStorage.getItem("duckDerbyBestScore"));

        //check if the bestScore variable is available in
        if(!prevBestScore) {
            prevBestScore = 0;
            localStorage.setItem("duckDerbyBestScore", 0);
        }

        if(totalScore > prevBestScore)
        {
            localStorage.setItem("duckDerbyBestScore", totalScore);
            if(prevBestScore!=0){
                recordScore = true;
            }
        }
    } else {
        // Sorry! No local Storage support..
        alert('This version of local web browser does not support local storage');
    }
}

function showOverlay(overlayType) {
    var overlay = document.createElement("div"),
        random;
    overlay.setAttribute("id", "overlay");
    overlay.setAttribute("class", "overlay");
    document.body.appendChild(overlay);


    var createOverlayDiv = function(divId){
        var message_div = document.createElement("div");
        message_div.setAttribute("id", divId);
        message_div.setAttribute("class", "center");
        return message_div;
    }

    overlay.appendChild(createOverlayDiv("mdiv"));


    var actionButton = document.createElement("input");
    actionButton.setAttribute("type", "button");

    actionButton.setAttribute("id", "but");
    actionButton.setAttribute("class", "btn center");
    actionButton.setAttribute("style", "font-family: Arial");
    if (overlayType == "continue") {
        actionButton.setAttribute("onclick", "reload()");
        actionButton.setAttribute("value", "Continue");
        document.getElementById("mdiv").innerHTML = "Congratulations!! You saved your ducks";
    } else {
        actionButton.setAttribute("onclick", "finalView()");
        actionButton.setAttribute("value", "Play Again");
        document.getElementById("mdiv").innerHTML = "You lost in this level!! But there is always next time :) !!";
        if(recordScore){
            overlay.appendChild(createOverlayDiv("highscdiv"));
            document.getElementById("highscdiv").innerHTML = "Congratulations! You have a new high score: "+parseInt(localStorage.getItem("duckDerbyBestScore"));
            recordScore = false;
        }
        random = Math.round(Math.random()*6);
        overlay.appendChild(createOverlayDiv("factsdiv"));
        document.getElementById("factsdiv").innerHTML = "Fun Fact: " + facts[random];


    }
    overlay.appendChild(actionButton);
}

function reload() {
	removeIfAnyExtraneousDivs();
    localStorage.setItem("gameLevel", (level + 1));
    location.reload();
}

function finalView() {
	removeIfAnyExtraneousDivs();
    localStorage.removeItem("totalScore");
    localStorage.removeItem("gameLevel");
    location.reload(true);
}

function removeIfAnyExtraneousDivs() {
	var overlay = document.getElementById('overlay');
  	if(overlay) {
		var but = document.getElementById('but');
		var message_div = document.getElementById('mdiv');
		if(but){
		    overlay.removeChild(but);
		}
		if(message_div){
		    overlay.removeChild(message_div);
		}
		document.body.removeChild(overlay);
	}
}

function toggleSound() {
    soundOn = !soundOn;
    localStorage.setItem("duckDerbySoundOn", soundOn);
}

function collisionHandler(obj1, obj2){
    if (soundOn) {
        quack.play();
    }
}

