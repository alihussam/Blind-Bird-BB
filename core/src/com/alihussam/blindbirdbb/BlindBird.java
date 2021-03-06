package com.alihussam.blindbirdbb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;


public class BlindBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private Texture birds[];
	private Texture topTube;
	private Texture bottomTube;
	private BitmapFont gameOver;
	private int flapState = 0;
	private float gap = 400;
	private int gameState = 0; //0 means not playing
    private float velocity = 0;
    private double gravity = 1.5;
	private float birdY;
	private float birdX;
	private int score = 0;
	private int scoringTube = 0;
	//private ShapeRenderer shapeRenderer;
	private Circle birdCircle;
	private Rectangle[] topTubeRectangles;
	private Rectangle[] bottomTubeRectangles;
    private BitmapFont font;
	private float tubeVelocity = 4;
	private int noOfTubes = 4;
	private float[] tubeX = new float[noOfTubes];
    private float[] tubeOffset = new float[noOfTubes];
	private float distanceBetweenTubes;
	private Random randomGenerator;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		birdX = Gdx.graphics.getWidth()/2 - birds[0].getWidth()/2;
	    distanceBetweenTubes = Gdx.graphics.getWidth()*(float)(0.6);
        randomGenerator = new Random();
        topTubeRectangles = new Rectangle[noOfTubes];
        bottomTubeRectangles = new Rectangle[noOfTubes];
        start();
	    //shapeRenderer = new ShapeRenderer();
	    birdCircle = new Circle();
	    font = new BitmapFont();
	    font.setColor(Color.WHITE);
	    font.getData().setScale(10);
	    gameOver = new BitmapFont();
	    gameOver.setColor(Color.WHITE);
	    gameOver.getData().setScale(20);


	}

	private void start(){
        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
        for(int i=0; i < noOfTubes ;i++){
            tubeOffset[i] = (randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth() + i*distanceBetweenTubes;
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

	@Override
	public void render () {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(gameState == 1) {
            if(tubeX[scoringTube] < birdCircle.x){
                score++;
                if(scoringTube < noOfTubes - 1){
                    scoringTube++;
                }else{
                    scoringTube = 0;
                }
            }
            if(Gdx.input.justTouched()) {
                velocity = -25;
            }
            for(int i=0; i<noOfTubes ; i++){
                if(tubeX[i]< -topTube.getWidth()){
                    tubeX[i] += noOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                }else {
                    tubeX[i] -= tubeVelocity;
                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + tubeOffset[i]);
                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],
                        topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight(),
                        bottomTube.getWidth(), bottomTube.getHeight());

            }
            if(birdY > 0) {
                velocity += gravity;
                birdY -= velocity;
            }else{
                gameState = 2;
            }
        }
        else if(gameState == 0){
            if(Gdx.input.justTouched())
                gameState = 1;
        }else{
            font.draw(batch, "Game Over", Gdx.graphics.getWidth()/2 - 350, Gdx.graphics.getHeight()/2 - 50);
            if(Gdx.input.justTouched()){
                gameState = 0;
                start();
                scoringTube = 0;
                velocity = 0;
                score = 0;
            }
        }

        if (flapState == 0) flapState = 1;
        else flapState = 0;

        batch.draw(birds[flapState], birdX, birdY);
        font.draw(batch, String.valueOf(score), 100, 200);
        batch.end();
        birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2,
                birds[flapState].getWidth()/2);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.BLACK);
//        shapeRenderer.circle(birdCircle.x,birdCircle.y, birdCircle.radius);
        for(int i = 0; i < noOfTubes; i++){
            if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) ||
                    Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){
                gameState = 2; //gameOver
            }
        }
//        shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
