"use strict";

function CreateOperation(eval_, str_, diffEval_) {
    function AbstractOperation(...args) {
        this.operands = args;
    }
    AbstractOperation.prototype = Object.create(CreateOperation.prototype);
    AbstractOperation.prototype.eval = eval_;
    // :NOTE: str_
    AbstractOperation.prototype.str = str_;
    AbstractOperation.prototype.diffEval = diffEval_;
    AbstractOperation.len = eval_.length === 0 ? Infinity : eval_.length;

    return AbstractOperation;
}

CreateOperation.prototype.evaluate = function (...args) {
    return this.eval(...this.operands.map(arg => arg.evaluate(...args)));
};
CreateOperation.prototype.diff = function (dname) {
    let diffOps = this.operands.map(arg => arg.diff(dname));
    return this.diffEval(...this.operands.concat(diffOps));
};
CreateOperation.prototype.toString = function () {
    return this.operands.join(" ") + " " + this.str;
};
CreateOperation.prototype.postfix = function () {
    return "(" + this.operands.map(arg => arg.postfix()).join(" ") + " " + this.str + ")";
};
CreateOperation.prototype.prefix = function () {
    return "(" + this.str + " " + this.operands.map(arg => arg.prefix()).join(" ") + ")";
};

const Add = new CreateOperation((x, y) => x + y, "+", (x, y, dx, dy) => new Add(dx, dy));

const Subtract = CreateOperation((x, y) => x - y, "-", (x, y, dx, dy) => new Subtract(dx, dy));

const Multiply = CreateOperation((x, y) => x * y, "*", (x, y, dx, dy) => new Add(
    new Multiply(x, dy),
    new Multiply(y, dx))
);

const Divide = CreateOperation((x, y) => x / y, "/", (x, y, dx, dy) => new Divide(
    new Subtract(
        new Multiply(y, dx),
        new Multiply(x, dy)
    ),
    new Multiply(y, y))
);

const Log = CreateOperation(
    (x, y) => Math.log(Math.abs(y)) / Math.log(Math.abs(x)),
    "log",
    (x, y, dx, dy) => new Divide(
        new Subtract(
            new Multiply(dy, new Multiply(x, new Log(E, x))),
            new Multiply(dx, new Multiply(y, new Log(E, y)))
        ),
        new Multiply(
            new Multiply(new Log(E, x), new Log(E, x)),
            new Multiply(x, y)
        )
    ));

const Pow = CreateOperation(Math.pow, "pow", (x, y, dx, dy) => new Add(
    new Multiply(
        new Pow(x, new Subtract(y, ONE)),
        new Multiply(dx, y)),
    new Multiply(
        new Log(E, x),
        new Multiply(dy, new Pow(x, y)))
));


const Negate = CreateOperation(x => -x, "negate", (x, dx) => new Negate(dx));

const Mean = CreateOperation((...args) => mean(...args), "mean", (...args) => {
        let diffOperands = args.slice(args.length / 2, args.length);
        return new Mean(...diffOperands);
    }
);
function mean(...args) {
   return args.reduce((a, b) => a + b, 0) / args.length;
}
const Var = CreateOperation(
    (...args) => {
        let op2 = [];
        for (let i = 0; i < args.length; i++) {
            op2.push(args[i] * args[i]);
        }
        return mean(...op2) - mean(...args) * mean(...args);
    },
    "var",
    (...args) => {
        let operands = args.slice(0, args.length / 2);
        let diffOperands = args.slice(args.length/2, args.length);
        let sumOpDiffOp = [];
        for (let i = 0; i < operands.length; i++) {
            sumOpDiffOp.push(new Multiply(operands[i], diffOperands[i]));
        }
        return new Subtract(
            new Multiply(TWO, new Mean(...sumOpDiffOp)),
            new Multiply(TWO, new Multiply(new Mean(...operands),
                new Mean(...diffOperands))));
    }
);


const OPS = {
    "+" : Add,
    "-" : Subtract,
    "/" : Divide,
    "*" : Multiply,
    "negate" : Negate,
    "log" : Log,
    "pow" : Pow,
    "mean" : Mean,
    "var" : Var
};

const ONE = new Const(1);
const ZERO = new Const(0);
const TWO = new Const(2);
const E = new Const(Math.E);

function Const(x) {
    this.x = x;
}
Const.prototype.evaluate = function() {
    return this.x;
};
Const.prototype.postfix = function() {
    return this.x.toString();
};
Const.prototype.toString = function() {
    return this.x.toString();
};
Const.prototype.prefix = function() {
    return this.x.toString();
};
Const.prototype.diff = function() {
    return ZERO;
};

let VARS = ["x", "y", "z"];
function Variable(name) {
    this.name = name;
}
Variable.prototype.evaluate = function(...args) {
    const index = VARS.indexOf(this.name);
    return args[index];
};
Variable.prototype.postfix = function() {
    return this.name;
};
Variable.prototype.toString = function() {
    return this.name;
};
Variable.prototype.prefix = function() {
    return this.name;
};
Variable.prototype.diff = function(dname) {
    return this.name === dname ? ONE : ZERO;
};

let parse = function (str) {
    let ops = str.trim().split(/\s+/);
    let stack = [];
    while (ops.length !== 0) {
        let t = ops.shift();
        if (OPS[t] !== undefined) {
            stack.push(new OPS[t](...stack.splice(-OPS[t].len)));
        } else if (!isNaN(parseInt(t))) {
            stack.push(new Const(parseInt(t)));
        } else if (VARS.includes(t)){
            stack.push(new Variable(t))
        }
    }
    return stack.pop();
};

let parsePrefixOrPostfix = function (mode, str) {
    function test(expected) {
        return source[pos] === expected;
    }

    function update() {
        pos++;
        while (pos >= 0 && pos < source.length && source[pos] === ' ') pos++;
    }

    function next() {
        let i1 = pos + 1;
        while (i1 < source.length && source[i1] !== ' ' && source[i1] !== ')' && source[i1] !== '(') {
            i1++;
        }
        let res = source.substring(pos, i1);
        pos = i1 - 1;
        update();
        return res;
    }

    function parseToken(str) {
        if (!isNaN(str)) {
            return new Const(parseInt(str));
        } else if (VARS.includes(str)) {
            return new Variable(str);
        } else {
            throw InvalidTokenError(str);
        }
    }

    function parseArguments() {
        let operands = [];
        let token = "";
        while (!test(')') && pos !== source.length) {
            if (test('(')) {
                operands.push(parsing());
                update();
            } else {
                token = next();
                if (mode === "postfix" && OPS[token] !== undefined) {
                    operands.push(token);
                    break;
                }
                operands.push(parseToken(token));
            }
        }
        return operands;
    }

    function checkingEndOfExpression(op, operands) {
        if (OPS[op].len !== Infinity && OPS[op].len !== operands.length) {
            throw WrongNumberOfArgsError(OPS[op].len, operands.length);
        }
        if (!test(')')){
            throw MissingClosingBracketError;
        }
    }

    function parsing () {
        update();
        let op;
        let operands = [];
        if (mode === 'prefix') {
            op = next();
            operands = parseArguments();
        } else if (mode === 'postfix') {
            operands = parseArguments()
            op = operands.pop()
        }
        if (OPS[op] === undefined) {
            throw UnknownOperationError(op === undefined ? "" : op);
        }
        checkingEndOfExpression(op, operands);
        return new OPS[op](...operands);
    }

    let source = str.trim();
    let pos = 0;
    if (source.length === 0) {
        throw EMPTY_INPUT_ERROR;
    }
    if (!test('(')) {
        return parseToken(source);
    }
    let result = parsing();
    if  (pos !== source.length - 1) {
        throw ExcessiveInfoError;
    }
    return result;
};

// :NOTE: "postfix"

let parsePostfix = (str) => parsePrefixOrPostfix("postfix", str);
let parsePrefix = (str) => parsePrefixOrPostfix("prefix", str);

const InvalidTokenError = (str) => new ParsingError("Expected variable or number, found  " + str);
const UnknownOperationError = (str) => new ParsingError("Expected operation, found \"" + str + "\"");
const WrongNumberOfArgsError = (exp, n) => new ParsingError("Wrong number of arguments: expected " + exp + ", found " + n);

const ExcessiveInfoError = new ParsingError("Excessive info");
const EMPTY_INPUT_ERROR = new ParsingError("Empty input");
const MissingClosingBracketError = new ParsingError("Missing closing bracket");

function ParsingError(message) {
    Error.call(this, message);
    this.message = message;
}
ParsingError.prototype = Object.create(Error.prototype);
ParsingError.prototype.constructor = ParsingError;
ParsingError.prototype.name = "ParsingError";
