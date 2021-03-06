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
##Patterns
#####Patterns are made from Units
A Unit is simply a value with a given start time.
Both units and patterns share the same Class.
They both have the same set of modifiers.
They _are_ the same, just that Patterns contain units, or perhaps more patterns.

####Here is the hard part:
Due to the nature of patterns in MuseLang, the timing of pattern units are innately variable.

Here are some possible issues that will arise with this:

####Determining the time span of a unit

As for timing, patterns are separated by bars using the pipe character | as the 'bar lines'.
Each bar will take an equal time to complete based on the bpm and timesig.
Each unit of a pattern will take an equal amount of time.
That is to say, in the pattern:

`\a a {a a} (a a [a a al4*2]l0.5 a)v(rnd 0.5 0.8) | bl4 \`

The time span of the first 'a' is equal to an eleventh of the time span of one bar.

####Determining the time span of a unit, with other absolute-timed units sharing the same bar.

However, there is an exception. Examining the following pattern:

`\al'3.5 ahp0.5 ahp0.8\`

Assuming that the timesig is set to 4 beats a bar,
the first 'a' _must_ take up the span of 3.5 beats. So, the last two a's will have 0.5 beats to share,
so each 'a' will have 0.25 beats each.

####Time span of a unit with modified relative time

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

####Too many beats in a bar

`\al'4 al'3 a al2 a | al'3 a ac0.5 \`

Assuming that one bar has 4 beats _again..._ The whole 4 beats has been taken up by the first 'a'.
And another extra 3 has been taken by the second 'a'.
In such a case, treat the absolute time no longer as an absolute time, but a relative time.
But still treat the relative time as relative time.
Now the first bar has a total of 11 units of equal time, but not absolute time.
Finally, condense the time of the 11 units to make it fit into 4 beats. Now you have undeciplets.

####Too little beats in a bar

`\[al'1.5 al'0.5 | ]*2\`

This Pattern expands to `\al'1.5 al'0.5 | al'1.5 al'0.5\`.
There is a total of 2 absolute beats in each bar, and no more to fill up the extra 2 beats of space.
In this case, just fill up that extra 2 beats with nothingness.

####Variable time

`\[al'(rnd 0.5 1) (a a a a)l'(rnd 1 2) a*(rndint 1 2)]\`

As you can see here, real-time variable parameters of units are a very hard thing to accomplish.
For such a feature to actually work, the `rnd` function needs to be re-evaluated every iteration.
And because the parameters can even affect the number of units in a pattern, the whole pattern needs to be
re-evaluated every iteration.

So, although this MuseLang is technically a 'live code', its patterns and their units are completely
evaluated and scheduled not spontaneously when the time calls for, but at the start of every pattern
iteration/reiteration.

####Mid-pattern changes

During a pattern eval, the only extra process done is an eval of the pattern itself.
No need to keep time etc as pattern stamps are all kept in the pattern.

####Tempo Change

During a tempo change, patterns need not be evaluated as the output of pattern evaluation will its custom
scheduled events in the unit of beats.

####Time Signature Change

*time signature refers to the numerator of a real time signature, as changing the rhythmical denominator
in this contexts is arbitrary.

During a time signature change code evaluation, the changes will only take place after this current bar has completed.
This is for obvious musical reasons.

Re-evaluation of Patterns will need to take place as rhythms of its patterns are heavily dependant on time signature
(aka beats in a bar...), as they are scheduled in the unit of beats. The existing scheduling of notes will be canceled.

In this case, all code must be evaluated as soon as possible, but in advance such that by the end of
the current bar, all scheduling is complete and can continue naturally until the end of the pattern reaches.

If the current bar is already the end of the pattern, don't make any changes to the scheduling.

##Code
###Evaluation
Since MuseLang is a livecoding language, syntax errors will be very common.
A key feature (or necessity) would be for MuseLang to evaluate only the necessary code
as intended by the user.

There are two ways the user can specify what code to evaluate:

####Caret Position
When code is evaluated without a selection, the code evaluated will be the zero-depth code which
encompasses the caret position.

####Code Selection
When code is evaluated with a selection, the code evaluated will be the entire selection,
plus the code before the selection to reach zero-depth, plus the code after the selection to
reach zero-depth.

####Zero-depth?
By right, zero-depth are the char indexes in code of where the number of unclosed parenthesis 
(or whatever grouping delimiter) _should_ be zero. However, a semi-typed function call
or perhaps an additional bracket somewhere before the caret or selection will cause the actual zero-depth
to be either the entire code, or just a single unit of depth short from the correct one.

To solve this, do recursive delimiter matching. This is where 'zero-depth' becomes the lowest possible depth
with a proper matching delimiter.

However, this is a CPU consuming process and cannot be done while being perceived as instantaneous by the user.
So, this process of parsing will be done 50ms after the last keystroke - where the code is
split into sections of depth, and the start-end char index range of the zero-depth sections can
be efficiently iterated through to find which section matches the cursor. The above mentioned process
and actual parsing, will be done at regular intervals of 400ms, or 200ms after a keystroke. _Hopefully_

#Syntax

Syntax is mainly split into two types
- Pattern Syntax - A non-extensible pattern-returning literal
- Main Syntax - Something that looks like lisp, but is actually an OOP.

##Pattern Syntax:
####TODO

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
