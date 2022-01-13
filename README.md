# exprl_java
A simple expression language written in Java.

## Syntax

**Literals**

- `true` or `false` for booleans
- `42`, `31.4` or `-1` for numbers
- `'hello world'` for text
- `variable-name` for literal variables

**Functions**

- `function(argA)` for unary functions
- `function(argA, argB)` for binary functions
- `function(argA, argB, ..., argN)` for n-ary functions

Functions and literals can be combined to express complex expressions :

- `concat('abc', 'def', 'ghi')`
- `cond(and(true, true), 'abcdef', '')`
- `len(substr('test', 0, sub(len('test'), 1)))`

## Available functions

- `not(argA: boolean): boolean` : logical NOT
  - `not(true)` ==> `false`
  - `not(false)` ==> `true`
- `and(argA: boolean, argB: boolean, ...argN: boolean): boolean` : logical AND
  - `and(true, true)` ==> `true`
  - `and(true, true, false)` ==> `false`
- `or(argA: boolean, argB: boolean, ...argN: boolean): boolean` : logical OR
  - `or(false, false)` ==> `false`
  - `or(true, true, false)` ==> `true`
- `add(argA: number, argB: number, ...argN: number): number` : addition
  - `add(1, 2)` ==> `3`
  - `add(1, -2, 3)` ==> `2`
- `sub(argA: number, argB: number): number` : substraction
  - `sub(1, 2)` ==> `-1`
  - `sub(3, 1)` ==> `2`
- `mul(argA: number, argB: number, ...argN: number): number` : multiplication
  - `mul(1, 2)` ==> `2`
  - `mul(1, -2, 3)` ==> `-6`
- `div(argA: number, argB: number): number` : division
  - `div(9, 3)` ==> `3`
  - `div(9, 2)` ==> `4.5`
- `mod(argA: number, argB: number): number` : modulus division
  - `mod(9, 3)` ==> `0`
  - `mod(9, 2)` ==> `1`
- `starts(str: text, prefix: text): boolean` : textual starts with check
  - `starts('abcdef', 'a')` ==> `true`
  - `starts('abcdef', 'ab')` ==> `true`
  - `starts('abcdef', 'f')` ==> `false`
- `ends(str: text, suffix: text): boolean` : textual ends with check
  - `ends('abcdef', 'a')` ==> `false`
  - `ends('abcdef', 'ab')` ==> `false`
  - `ends('abcdef', 'f')` ==> `true`
- `in(str: text, content: text): boolean` : textual contains check
  - `in('abcdef', 'a')` ==> `true`
  - `in('abcdef', 'ab')` ==> `true`
  - `in('abcdef', 'cd')` ==> `true`
  - `in('abcdef', 'z')` ==> `false`
- `substr(str: text, begin: number, end: number): text` : textual sub-string extraction based on indexes
  - `substr('abcdef', 1, 5)` ==> `'bcde'`
  - `substr('abcdef', 0, -1)` ==> `abcdef`
  - `substr('abcdef', 1, -2)` ==> `bcde`
- `substrl(str: text, begin: number, end: number): text` : textual sub-string extraction based on index and length
  - `substrl('abcdef', 1, 5)` ==> `'bcdef'`
  - `substrl('abcdef', -3, 2)` ==> `'ef'`
- `concat(str: text, append: text): text` : textual concatenation
  - `concat('abc', 'def')` ==> `'abcdef'`
- `len(str: text): number` : textual length
  - `len('abcdef')` ==> 6
  - `len('abc')` ==> 3
- `eq(argA: any, argB: any): boolean` : equality check
  - `eq('abcdef', 'abcdef')` ==> `true`
  - `eq(1, 1)` ==> `true`
  - `eq(1, 2)` ==> `false`
- `neq(argA: any, argB: any): boolean` : inequality check
  - `neq('abcdef', 'abcdef')` ==> `false`
  - `neq(1, 1)` ==> `false`
  - `neq(1, 2)` ==> `true`
- `lt(argA: number, argB: number): boolean` : less than check
  - `lt(1, 2)` ==> `true`
  - `lt(2, 2)` ==> `false`
  - `lt(3, 2)` ==> `false`
- `lte(argA: number, argB: number): boolean` : less than or equal to check
  - `lte(1, 2)` ==> `true`
  - `lte(2, 2)` ==> `true`
  - `lte(3, 2)` ==> `false`
- `gt(argA: number, argB: number): boolean` : greater than check
  - `gt(1, 2)` ==> `false`
  - `gt(2, 2)` ==> `false`
  - `gt(3, 2)` ==> `true`
- `gte(argA: number, argB: number): boolean` : greater than or equal to check
  - `gte(1, 2)` ==> `false`
  - `gte(2, 2)` ==> `true`
  - `gte(3, 2)` ==> `true`
- `min(argA: number, argB: number, ...argN: number)`: minimum of given numbers
  - `min(0, 1)` ==> `0`
  - `min(0, -1, -5)` ==> `-5`
- `max(argA: number, argB: number, ...argN: number)`: maximum of given numbers
  - `max(0, 1)` ==> `1`
  - `max(0, -1, 5)`==> `5`
- `var(variable: text): any`: access the content of a variable
  - `var('variable-boolean')` ==> `true` with `variable-boolean` set to `true`
  - `var('variable-text')` ==> `'abcdef'` with `variable-text` set to `'abcdef'`
- `cond(condition: boolean, then: any, else: any): any` : conditional selection between two expressions
  - `cond(true, 1, 2)` ==> `1`
  - `cond(false, 1, 2)` ==> `2`
  - `cond(false, 'a', 'b')` ==> `'b'`
- `debug(debugged: any, label: text): any`: reports the result of the execution as a labeled debug layer and transparently forwards the result to the caller
  - `debug(true, 'dbg1')` ==> `true` + reports `dbg1 : true` to the context
  - `debug(cond(true, 1, 2), 'dbg-cond')` ==> `1` + reports `dbg-cond : 1` to the context
  - `debug(cond(debug(and(true, true), 'dbg-and'), 'abcdef', ''), 'dbg-cond')` ==> `'abcdef'` + reports `dbg-and : true` and `dbg-cond : abcdef` to the context

## Accessing variables

Variables can either be accessed using `var('variable-name')` function or by using literal variable like `variable-name`. For example `and(var('variable-name'), true)` and `and(variable-name, true)` are equivalent.

However, accessing a dynamically named variable (i.e. a variable whose name is built from sub-expressions) can only be accessed using `var()` function. For example `var(concat('constant-prefix.', variable-part))`. 
 
## Usage

```java
ExpressionEvaluator evaluator = new ExpressionEvaluator();
evaluator.setVariable("state", true);

// Parse and evaluate expression
boolean result = evaluator.evaluateBoolean("var('state')");

// Parse expression
Expression parsedExpression = Parser.parse("var('state')");

// Evaluate already parsed expression
result = evaluator.evaluateBoolean(parsedExpression);
```

The evaluator guarantees that variables will keep consistent during the evaluation of an expression.
