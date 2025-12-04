grammar LabelRule;

// 1. 入口
parse
    : expression EOF
    ;

// 2. 递归逻辑表达式
expression
    : '(' expression ')'               # ParenExpr
    | expression AND expression        # AndExpr
    | expression OR expression         # OrExpr
    | atom                             # AtomExpr
    ;

// 3. 原子条件 (核心修改点)
atom
    : field op value                   # BinaryOpAtom  // 处理 =, >, < 等单值场景
    | field IN '(' valueList ')'       # InOpAtom      // 处理 IN (1,2,3) 多值场景
    ;

// 4. 字段定义 (保持命名空间)
field
    : tagType '.' tagCode
    ;

tagType : ID ;
tagCode : ID ;

// 5. 值列表定义
valueList
    : value (',' value)*
    ;

value
    : INT
    | STRING   // 带引号的字符串 'male'
    | FLOAT
    ;

op
    : GT | GTE | LT | LTE | EQ | NEQ
    ;

// Lexer 规则
AND : 'AND' | 'and' | '&&';
OR  : 'OR'  | 'or'  | '||';
IN  : 'IN'  | 'in';
GT  : '>';
GTE : '>=';
LT  : '<';
LTE : '<=';
EQ  : '=' | '==';
NEQ : '!=';

ID  : [a-zA-Z_][a-zA-Z0-9_]*;
INT : [0-9]+;
FLOAT : [0-9]+ '.' [0-9]+;
STRING : '\'' .*? '\'';
WS  : [ \t\r\n]+ -> skip;