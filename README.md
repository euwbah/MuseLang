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
###The heart of Patterns are units.
A Unit is simply a value with a given start time.
Both units and patterns share the same Class.
They both have the same set of modifiers.
They _are_ the same, just that Patterns contain units, or perhaps more patterns.

####Here is the hard part:
Scheduling a pattern.
As of yet, MuseLang's patterns are scheduled spontaneously.
However, this may change in the future when note offset becomes a thing.

As for timing, patterns are separated by bars using the pipe character | as the 'bar lines'.
Each bar will take an equal time to complete based on the bpm and timesig.
Each unit of a pattern will take an equal amount of time.
That is to say, in the pattern:

`\a a {a a} (a a [a a al4*2]l0.5 a)v(rnd 0.5 0.8) | bl4 \`

The time span of the first 'a' is equal to an eleventh of the time span of one bar.

However, there is an exception. Examining the following pattern:

`\al'3.5 ahp0.5 ahp0.8\`

Assuming that the timesig is set to 4 beats a bar,
the first 'a' _must_ take up the span of 3.5 beats. So, the last two a's will have 0.5 beats to share,
so each 'a' will have 0.25 beats each.

And another example:

`\al'1 a a al'2 al2\`

Assuming that one bar has 4 beats again. In this case, the first a must be 1 beat long, and 
the fourth a must be 2 beats long. The fifth then has a length multiplier of two, so it should
take twice as long as however long the length of a generic 'a'. In this case, four, not three 
generic 'a's share a remaining 1 beat. So the second and third 'a' gets 0.25 beats each,
and the final a gets twice that.

Based on all the examples, one can tell that the order of operation in order to determine the absolute
length of a unit in a pattern is this:
- Determine all the units with a given absolute time
- Determine all the units with a given time relative to the generic unit
- Solve for the absolute time of a generic unit by taking remaining time to spare divided by total spans of generic units.
- Finally apply the relative time multipliers to generic units.

However, an issue can easily arise in such a case as this:

`\al'4 al'3 a al2 a | al'3 a ac0.5 \`

Assuming that one bar has 4 beats _again..._ The whole 4 beats has been taken up by the first 'a'.
And another extra 3 has been taken by the second 'a'.
In such a case, treat the absolute time no longer as an absolute time, but a relative time.
But still treat the relative time as relative time.
Now the first bar has a total of 11 units of equal time, but not absolute time.
Finally, condense the time of the 11 units to make it fit into 4 beats. Now you have undeciplets.

#Syntax

Syntax is mainly split into two types
- Pattern Syntax - A non-extensible pattern-returning literal
- Main Syntax - Something that looks like lisp, but is actually an OOP.

##Pattern Syntax:
###Generic Pattern Syntax:
These encapsulate both NotePatterns and ParamPatterns.
- 

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
(loopbe 1 \[c4l(rnd 0.3 0.5)v0.8 eb4v'86 d4 eb4p0.5]v'(rnd 64 76)*4p'95\
        (velocity ~\0.6 | 0.8p0.5\ (extend 2)) (mirror) (once))
```
