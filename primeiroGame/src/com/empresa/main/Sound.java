package com.empresa.main;

import java.applet.Applet;

import java.applet.AudioClip;

//import java.io.*;
//import javax.sound.sampled.*;

public class Sound {
	
	//metodo minecraft
	/*
	public static class Clips{
		public Clip[] clips;
		private int p;
		private int count;
		
		
		public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
			if(buffer == null) 
				return;
				
			clips = new Clip[count];
			this.count = count;
			
			for(int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer))); 
			}	
		}
		
		public void play() {
			
			if(clips == null)
				return;
			
			clips[p].stop();
			clips[p].setFramePosition(0);
			clips[p].start();
			p++;
			
			if(p >= count)
				p = 0;
		}
		
		public void loop() {
			
			if (clips == null) 
				return;
			
			clips[p].loop(300);
		}
		
	}
	
	public static Clips musicBackground = load("/menuBackground.wav", 1);
	public static Clips hurtEffect = load("/hurt.wav", 1);
	public static Clips zombieHurt = load("/zombieHurt.wav", 1);
	public static Clips menuChangeItem = load("/menuChangeitem.wav", 1);
	public static Clips shootEffect = load("/shoot3.wav", 1);

	
	private static Clips load(String name, int count) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(name));
			
			byte[] buffer = new byte[1024];
			int read = 0;
			
			while((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0, read);
			}
			
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(data, count);
			
		}catch(Exception e){
			try {
				return new Clips(null, 0);
			}catch(Exception ee) {
				return null;
			}
		}
	}
*/               //metodo inicial mais simples
	
	private AudioClip clip;
	
	public static Sound musicBackground = new Sound("/menuBackground.wav");
	public static final Sound hurtEffect = new Sound("/hurt.wav");
	public static final Sound shootEffect = new Sound("/shoot3.wav");
	public static final Sound emptyGun = new Sound("/emptyGun.wav");
	public static final Sound takeGun = new Sound("/takeGun.wav");
	public static final Sound takeAmmo = new Sound("/takeAmmo.wav");
	public static final Sound pickLife = new Sound("/pickLife.wav");
	public static final Sound takeBulletEffect = new Sound("/bullet.wav");
	public static final Sound takeLifeEffect = new Sound("/life.wav");
	public static final Sound menuChangeItem = new Sound("/menuChangeItem.wav");
	public static final Sound menuBackground = new Sound("/menuBackground.mp3");
	public static final Sound zombiesSound = new Sound("/zombiesSound.wav");
	public static final Sound hurt = new Sound("/hurt.wav");
	public static final Sound hurt2 = new Sound("/hurt2.wav");
	public static final Sound zombieHurt = new Sound("/zombiehurt.wav");
	
	
	
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
