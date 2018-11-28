# 拡張BNF LL1判定器
  
本プログラムでは、記述した拡張BNF記法がLL1であるかを判定する。  
出力結果は以下の通りである。

- 正常にLL1判定を行うため、元の拡張BNFから変更を行った拡張BNF  
- First集合
- Follow集合
- Director集合
- エラー又は訂正を行った内容
- 判定結果

なおマルチバイトには非対応である。

## 拡張BNF記述ルール

拡張BNF記法の記述方法として、以下の記述ルールを適用する。  
~~"ε"がマルチバイトなの本当に許さんからな~~

- 終端記号及び非終端記号は、半角英数字とアンダーバーからなる
- 空文字は、"~"によって表現される
- 等式については、"::="によって結ばれる
- コメントは、";"以降に記述する
- 「または」は、"|"によって表現される
- 1回の繰り返しは、"("と")"で囲む
- 0回以上の繰り返しは、"{"と"}"で囲む
- 0回または1回の繰り返しは、"["と"]"で囲む
- "(", "{", "["と"]", "}", )"で囲んだ場合、"|"は囲んだ中で適用される
- 特殊文字は、空白文字によって分けなくても自動で判別する 
- 出力する際、Follow集合が空である場合には"$"をFollow集合とする 

## 実行方法

~~2017/09/06現在、javac時に警告が出力される。~~  
2018/11/28、[Pull Request](https://github.com/who3411/isLL1/pull/1)をmergeし、警告が出力されないようになりました。ありがとうございます。

```
git clone git://github.com/who3411/isLL1.git
cd isLL1
mkdir out
javac -d out -encoding UTF-8 -sourcepath src src/isLL1/LL1Check.java
java -classpath out isLL1.LL1Check /path/to/EBNF/file
```

※/path/to/EBNF/fileの部分は、各自適切な拡張BNFが記述されたファイルのパスを入力

## 実行例

以下に4つの実行例を載せる。それぞれ次の場合の実行結果である。

1. LL1である場合(testcase/test1.txt)
1. warningが発生かつLL1である場合(testcase/test2.txt)
1. LL1でない場合(testcase/test3.txt)
1. エラーが発生する場合(testcase/test4.txt)

### 実行例1

#### 記述した拡張BNF

```
CODE ::= { LINE } EOF
LINE ::= [ TEST ] NL
TEST ::= INST PARAM LOOP | END
LOOP ::= COMMA PARAM LOOP | ~
```

#### 実行結果

```
CODE ::= { LINE } EOF 
LINE ::= [ TEST ] NL 
TEST ::= INST PARAM LOOP | END 
LOOP ::= COMMA PARAM LOOP | ~ 
  
First(CODE) = {EOF, NL, INST, END}
First(LINE) = {NL, INST, END}
First(TEST) = {INST, END}
First(LOOP) = {COMMA, ~}
  
Follow(CODE) = {$}
Follow(LINE) = {EOF}
Follow(TEST) = {NL}
Follow(LOOP) = {NL}
  
Director(CODE, { LINE } EOF) = {NL, INST, END, EOF}
Director(LINE, [ TEST ] NL) = {INST, END, NL}
Director(TEST, INST PARAM LOOP) = {INST}
Director(TEST, END) = {END}
Director(LOOP, COMMA PARAM LOOP) = {COMMA}
Director(LOOP, ~) = {NL}
  
この拡張BNF記法はLL1です。
[error] 0 [warning] 0
```

### 実行例2

#### 記述した拡張BNF

```
CODE ::= { LINE } EOF
LINE ::= [ TEST ] NL
TEST ::= INST PARAM LOOP | END
LOOP ::= { COMMA PARAM }
```

#### 実行結果

```
[warning]
LOOP ::= { COMMA PARAM } 
空文字が発生する可能性があるので、空文字を追加します。
CODE ::= { LINE } EOF 
LINE ::= [ TEST ] NL 
TEST ::= INST PARAM LOOP | END 
LOOP ::= { COMMA PARAM } | ~ 
  
First(CODE) = {EOF, NL, INST, END}
First(LINE) = {NL, INST, END}
First(TEST) = {INST, END}
First(LOOP) = {COMMA, ~}
  
Follow(CODE) = {$}
Follow(LINE) = {EOF}
Follow(TEST) = {NL}
Follow(LOOP) = {NL}
  
Director(CODE, { LINE } EOF) = {NL, INST, END, EOF}
Director(LINE, [ TEST ] NL) = {INST, END, NL}
Director(TEST, INST PARAM LOOP) = {INST}
Director(TEST, END) = {END}
Director(LOOP, { COMMA PARAM }) = {COMMA}
Director(LOOP, ~) = {NL}
  
この拡張BNF記法はLL1です。
[error] 0 [warning] 1
```

### 実行例3

#### 記述した拡張BNF

```
CODE ::= { LINE } EOF
LINE ::= [ TEST ] NL
TEST ::= INST | END | LOOP
LOOP ::= ( INST | END ) LOOP
```

#### 実行結果

```
CODE ::= { LINE } EOF 
LINE ::= [ TEST ] NL 
TEST ::= INST | END | LOOP 
LOOP ::= ( INST | END ) LOOP 
  
First(CODE) = {EOF, NL, INST, END}
First(LINE) = {NL, INST, END}
First(TEST) = {INST, END}
First(LOOP) = {INST, END}
  
Follow(CODE) = {$}
Follow(LINE) = {EOF}
Follow(TEST) = {NL}
Follow(LOOP) = {NL}
  
Director(CODE, { LINE } EOF) = {NL, INST, END, EOF}
Director(LINE, [ TEST ] NL) = {INST, END, NL}
Director(TEST, INST) = {INST}
Director(TEST, END) = {END}
Director(TEST, LOOP) = {INST, END}
Director(LOOP, ( INST | END ) LOOP) = {INST, END}
  
Director(TEST, INST ) ∩ Director(TEST, INST END ) = {INST}
この拡張BNF記法はLL1ではありません。
[error] 0 [warning] 0
```

### 実行例4

#### 記述した拡張BNF

```
TEST ::= A { [ | ] | ( D | E ) } F
;TEST ::= [A [( B | C ) | ( D | E ) ] F ]
;TEST ::= A { { [B | C] | ( D | E ) } | { [ B | C ] | ( D | E ) } } F
B    ::= DATA
D    ::= TEST
```

#### 実行結果

```
[warning]
TEST ::= A { [ | ] | ( D | E ) } F 
空文字となる"|"があったので削除します。
[warning]
TEST ::= A { [ | ] | ( D | E ) } F 
空文字となる括弧があったので削除します。
[warning]
TEST ::= A { [ | ] | ( D | E ) } F 
空文字となる"|"があったので削除します。
[error]
TEST ::= A { ( D | E ) } F 
括弧が重複しています。
[error] 1 [warning] 3
```

## 作成者

who3411

## ライセンス

MIT(See LISENCE)

Copyright © 2017 who3411 All Rights Reserved.
