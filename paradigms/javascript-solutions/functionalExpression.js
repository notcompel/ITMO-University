"use strict";

let expression = op => {
    let operation = (...args) => (...vars) => op(...args.map(f => f(...vars)))
    operation.len = op.length;
    return operation;
};

let cnst = (x) => () => x;
let VARS = ["x", "y", "z"];
let variable = function (name) {
    const index = VARS.indexOf(name);
    return (...values) => values[index];
};

let subtract = expression((x, y) => x - y);
let add = expression((x, y) => x + y);
let multiply = expression((x, y) => x * y);
let divide = expression((x, y) => x / y);
let negate = expression((x) => -x);
let abs = expression((x) => Math.abs(x));
let iff = expression((x, y, z) => x >= 0 ? y : z);

const OPS = {
    "-" : subtract,
    "+" : add,
    "/" : divide,
    "*" : multiply,
    "negate" : negate,
    "abs" : abs,
    "iff" : iff
};

const pi = cnst(Math.PI);
const e = cnst(Math.E);
const CNSTS = {
    "pi" : pi,
    "e" : e
};

let parse = function (str) {
    let ops = str.trim().split(/\s+/);
    let stack = [];
    while (ops.length !== 0) {
        let t = ops.shift();
        if (OPS[t] !== undefined) {
            let vars = [];
            for (let i = 0; i < OPS[t].len; i++) {
                vars.unshift(stack.pop());
            }
            stack.push(OPS[t](...vars));
        } else if (!isNaN(parseInt(t))) {
            stack.push(cnst(parseInt(t)));
        } else if (CNSTS[t] !== undefined) {
            stack.push(CNSTS[t]);
        } else {
            stack.push(variable(t))
        }
    }
    return stack.pop();
};
