package com.example.test.audio.agent;


import com.example.test.audio.IAudioCustom;

public class MonoAgent extends BaseAudioCustomAgent {
    IAudioCustom iAudioCustom;


    public MonoAgent(IAudioCustom iAudioCustom) {
        this.iAudioCustom = iAudioCustom;
    }

    @Override
    protected void decodeAudio(byte[] audio) {

        if (iAudioCustom != null) {
            iAudioCustom.addAudioArray(audio);
        }

    }
}
