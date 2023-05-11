range_with_condition(N, L, C, [N | T]) :- G =.. [C, N, L], call(G), !, N1 is N + 1, range_with_condition(N1, L, C, T).
range_with_condition(N, L, C, []).

map_arg([], _, _, []).
map_arg([H | T], F, Arg, [HArg | TArg]) :- G =.. [F, H, HArg, Arg], call(G), map_arg(T, F, Arg, TArg).

map([], _, []).
map([H | T], F, [RH | RT]) :- G =.. [F, H, RH], call(G), map(T, F, RT).

power2(0, 0).
power2(1, 1).
power2(A, R) :-
   R is A * A.

checker(N, D) :- N is D ; \+ 0 is mod(N, D), !.
condition(N, MAX_N) :- N * N < MAX_N.

init(MAX_N) :- range_with_condition(2, MAX_N, condition, T), map(T, power2, TR), map_arg(TR, put_in_table, MAX_N, T).

put_in_table(N, K, MAX_N) :- assertz(composite_table(N)), N >= MAX_N, !.
put_in_table(N, K, MAX_N) :- assertz(composite_table(N)), Q is N + K, put_in_table(Q, K, MAX_N).

prime(N) :- N > 1, \+ composite_table(N).
composite(N) :- \+ prime(N).

concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).

is_equal([], []).
is_equal([H1 | T1], [H2 | T2]) :- H1 = H2, is_equal(T1, T2).

prime_divisors(N, [N]) :- integer(N), N > 1, prime(N), !.
prime_divisors(N, Divisors) :- integer(N), find_divisors(N, 2, V), list(Divisors), !,  is_equal(Divisors, V), !.
prime_divisors(N, Divisors) :- integer(N),  find_divisors(N, 2, Divisors), !.

find_divisors(N, Div, []) :- N < Div, !.
find_divisors(N, Div, [N]) :- prime(N), !.
find_divisors(N, Div, TR) :-
                add_div(N, Div, T1, N1),
                Q is Div + 1, find_divisors(N1, Q, T2), concat(T1, T2, TR).

add_div(N, Div, [], N) :- \+ 0 is mod(N, Div), !.
add_div(N, Div, TR, NR) :- N >= 2, 0 is mod(N, Div), N1 is N / Div, add_div(N1, Div, T1, NR), concat(T1, [Div], TR).

prime_divisors(N, Divisors) :- list(Divisors), mul(Divisors, 1, N, 1), !.
mul([], N, N, Prev).
mul([H | T], N, NR, Prev) :- Prev =< H, prime(H), N1 is N * H, mul(T, N1, NR, H).

digits(0, K, []) :- !.
digits(N, K, TR) :- Q is mod(N, K), N1 is div(N, K), digits(N1, K, T1), concat([Q], T1, TR).
prime_palindrome(N, K) :- digits(N, K, C), reverse(C, C), prime(N).