# MuseLang
'Programming' for the Musician
=============================

Program MIDI patterns seamlessly with multiple MIDI ports.

<Still a concept! Watch this space!>

There are many live coding music platforms out there, but most of them were intended for programmers.
Herein lies my attempt to create one for the Musician instead.

(Also I do know Sonic Pi exists, but it's technically not 'for the Musician' either, as it doesn't support MIDI
out of the box.)

Inspired by the robustness of Tidal's patterns, the simplicity of Sonic Pi, and the straightforwardness of
Wulfcode.


Here are some of my brainstorms, TODOs and other thoughts while I code this behemoth (at least it is to me...)

Platform:
Java FX for GUI
javax.sound.midi for MIDI
(Hopefully I can create my out simple code highlighter/indenter thingy so I don't have to learn more APIs :P)
(oh wait yes I can!) 

###Current Thoughts:
- Syntax
- Code Highlighting
- Parsing

###Design: 
currently only two code areas. One editable, the other is a console.

###Syntax ideas: 
Whitespace are all equal 
Lexing and parsing based on function argument count only 
Very basic scripting language

Alt + R to run at caret.
When interpreting, move backwards until a possible Initial Token is found,
and move forwards until just before the whitespace before the next Initial Token.
Use that range as the code to execute.
Use the same range for syntax highlighting on CodeArea change.

When a selection exists instead, check if the first word is a possible Initial Token,
if not, extend the selection backwards until the first possible Initial Token.
Similarly, check if the last word lies just before another Initial Token (excluding Whitespace),
if not, extend the selection forwards until just before the whitespace before the next Init Token.

##Tokens: 
###Initial tokens (Basically the beginning of the context used when lexing, anything before doesn't affect the context of what's to come)
- `linkport` 
- `listports` 
- `chd` 
- `defsuffixfunc`
- `bpm`
- `sig`
- Midi Target - `<portname> channel` 
- Identifiers

###Expression tokens:
- Unary Negative (only initial)
- Plus
- Minus
- Multiply
- Divide
- Power

###Main Syntax Tokens:
- Open/Close Parenthesis (the normal ones)
- Open/Close Brackets [The squarey ones]
- Open/Close Braces {The really weird ones}
- Whitespace

###Pattern tokens:
- NotePatternBeginToken `\....`
- NotePatternEnd `...\`
- ParamPattern Begin/End Token
- GroupEach Begin/End Token - in context of a pattern
- GroupWhole BeginEnd Token - ditto
- GroupSync Begin/End Token - ditto
- SuffixFuncToken - ditto as well
- NoteToken
- ParamToken

##Lexing/Parsing:
###Pattern
- NotePattern `\......\` 
- ParamPattern `~\.....\`
- GroupEach `(....)`
- GroupWhole `[...]`
- GroupSync `{...}`
- SuffixFunc 

###Expression
- X + Y

Example: 
`
bpm 120
sig 4

linkport "LoopBe Internal MIDI" LB
LB 1 \c4 c4 c4 c4\
`