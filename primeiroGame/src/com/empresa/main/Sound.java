package com.empresa.main;

import java.applet.Applet;

import java.applet.AudioClip;

public class Sound {
	
	private AudioClip clip;
	
	public static final Sound musicBackground = new Sound("/background.wav");
	//public static final Sound hurtEffect = new Sound("/hurt.wav");
	public static final Sound shootEffect = new Sound("/shoot3.wav");
	public static final Sound takeBulletEffect = new Sound("/bullet.wav");
	public static final Sound takeLifeEffect = new Sound("/life.wav");
	public static final Sound menuChangeItem = new Sound("/menuChangeItem.wav");
	public static final Sound menuBackground = new Sound("/menuBackground.mp3");
	public static final Sound zombiesSound = new Sound("/zombiesSound.wav");
	public static final Sound hurt = new Sound("/hurt.wav");
	public static final Sound hurt2 = new Sound("/hurt2.wav");
	public static final Sound zombiehurt = new Sound("/zombiehurt.wav");

	
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
			
		}catch(Throwable e) {
			
		}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		}catch(Throwable e) {
			
		}
	}
	
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		}catch(Throwable e) {
			
		}
	}

}
