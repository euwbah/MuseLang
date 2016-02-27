# MuseLang
'Programming' for the Musician
=============================

Program MIDI patterns seamlessly with multiple MIDI ports.

<Still a concept! Watch this space!>

There are many live coding music platforms out there, but most of them were intended for programmers.
Herein lies my attempt to create one for the Musician instead.

(Also I do know Sonic Pi exists, but it's technically not 'for the Musician' either, as it doesn't support MIDI
out of the box.)

Inspired by the robustness of Tidal's patterns, the simplicity of Sonic Pi, the straightforwardness of
Wulfcode, and the elegance of a lisp.


Here are some of my brainstorms, TODOs and other thoughts while I code this behemoth (at least it is to me...)

Platforms:
Java FX for GUI
javax.sound.midi for MIDI

###Current Thoughts:
- Syntax
- Code Highlighting
- Parsing

###Design: 
currently only two code areas. One editable, the other is a console.
remember to do file IO, and basic undo/redo.

###Syntax ideas:
Lisp

Alt + R to run at caret.
If no selection, evaluate entire contents of outermost parenthesis which the caret is in.
If selection exists, evaluate outermost parenthesis

#The Core
The heart of Patterns are units.
A Unit is simply a value with a given start time.



#Syntax

##Native Functions: 
- `linkport` 
- `listports` 
- `chd` 
- `defsuffixfunc`
- `bpm`
- `sig`

##Parsing:
###Pattern
- NotePattern `\......\` 
- ParamPattern `~\.....\`
- GroupEach `(....)`
- GroupWhole `[...]`
- GroupSync `{...}`
- SuffixFunc 

##Code Example: 
```
(bpm 120)//set bpm to 120
(sig 4)//set 4 beats in a bar

(linkport loopbe "LoopBe Internal MIDI")//link identifier loopbe to a midi port

//send pattern a c4 with 30%-50% of absolute note length and 80% of absolute velocity
//a eb4 with absolute velocity of 86/127, a d4 and a 50% of absolute chance of a eb4.
//Finally, set the absolute velocity to 64-76 for MidiSends without an
//existing absolute, repeat whatever I said four times over, then set the absolute probability to 95%
//for all the notes without an existing absolute probability. Do a final multiply
//of velocity using a parampattern which has 50% chance of alternating between
//0.6 and 0.8 every 2 bars, then mirror the entire pattern. Then play it once
//only, at the first instance of (ticks MOD 16 * PPQ == 0) immediately after the eval.

(updateChangesEvery 16 0)//every 16th beat, 0 offset.
(loopbe 1 \[c4l(r 0.3 0.5)v0.8 eb4v'86 d4 eb4p0.5]v'(r 64 76)*4p'95\
        (velocity ~\0.6 | 0.8p0.5\ (extend 2)) (mirror) (once))
```