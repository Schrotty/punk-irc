grammar IRC;

options
{
    language = Java;
}

/*
 * Parser Rules
 */
chat                : line+ EOF ;

line                : name command message NEWLINE ;

message             : (emoticon | link | color | mention | WORD | WHITESPACE)+ ;

name                : WORD WHITESPACE;

command             : (NICK) WHITESPACE WORD;

emoticon            : ':' '-'? ')'
                    | ':' '-'? '('
                    ;

link                : TEXT TEXT ;

color               : '/' WORD '/' message '/';

mention             : '@' WORD ;


/*
 * Lexer Rules
 */
fragment N          : ('N'|'n') ;
fragment I          : ('I'|'i') ;
fragment C          : ('C'|'c') ;
fragment K          : ('K'|'k') ;

fragment LOWERCASE  : [a-z] ;
fragment UPPERCASE  : [A-Z] ;

NICK                : N I C K;

WORD                : (LOWERCASE | UPPERCASE | '_')+ ;

WHITESPACE          : (' ' | '\t')+ ;

NEWLINE             : ('\r'? '\n' | '\r')+ ;

TEXT                : ('['|'(') ~[\])]+ (']'|')');