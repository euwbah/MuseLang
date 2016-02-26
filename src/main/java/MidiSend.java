import javax.sound.midi.ShortMessage;

public class MidiSend {
    public MidiMessageType messageType;

    public MidiSend() {

    }

    public enum MidiMessageType
    {
        NOTE_ON, CC
    }
}
