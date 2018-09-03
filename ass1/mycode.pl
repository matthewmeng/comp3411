%Name: Xiangzhuo Meng
%Zid: z5042679
%Assignment 1 - Prolog Programming


%Part1 sumsq_neg(Numbers, Sum) which sums the squares of only negative numbers in a list of numbers.

%sum should be 0 when input is NULL.
sumsq_neg([],0).

%positive input
sumsq_neg([Head|Tail],Sum):-
	sumsq_neg(Tail,The_sum),
	Head >= 0,
	Sum is The_sum.

%negative input
sumsq_neg([Head|Tail],Sum):-
	sumsq_neg(Tail,The_sum),
	Head < 0,
	Sum is The_sum + (Head * Head).




%Part2 all_like_all

%for testing





like_all(_,[]).

%if the person like all fruits
like_all(The_person,[H|T]):-
	likes(The_person,H),
	like_all(The_person,T).

all_like_all([],[]).
all_like_all([],_).

%if all person like all fruits.
all_like_all([Head_person|Tail_person],[Head_what|Tail_what]):-
	like_all(Head_person,[Head_what|Tail_what]),
	all_like_all(Tail_person,[Head_what|Tail_what]).






%Part3 sqrt_table(N,M,Result)

%When N == M
sqrt_table(N,M,Result):-
	N = M,
	X is sqrt(N),
	Result = [[N,X]].

%When N > M
sqrt_table(N,M,Result):-
	N > M,
	Curr = N,
	The_next is N -1,
	sqrt_table(The_next,M,Result_tail),
	X is sqrt(Curr),
	Result = [[Curr,X]|Result_tail].






%Part4 	chop_up(List,NewList)

%if the next element > current
head_next([A,B|_]):-
	B is A +1.

%When the list is not increasing_list return true.
increasing_list([Head,Next|Tail],[Head],[Next|Tail]):-
	not(head_next([Head,Next|Tail])).

%When the list is increasing_list return true and check the next List
%put the result in the 2nd augument, Remaining is the rest input list.
increasing_list([Head|Tail],[Head|Result],Remaining):-
	head_next([Head|Tail]),
	increasing_list(Tail,Result,Remaining).


increasing_list([Head|Tail],[Head|Tail],[]).


%merge_list the increasing_list
merge_list(List, NewList):-
	List = [_,_],
	NewList = List.

merge_list(List,NewList):-
	List = [Head|Tail],
	Tail = [_|TailT],
	merge_list([Head|TailT],NewList).

%check is the list just have one element.
one_element(List,New):-
	List = [A],
	New = A.


chop_up([], []).

chop_up(List, NewList) :-
    List = [_],
    NewList = List.

%for merging the increasing_list
chop_up(List, [New|NewList]) :-
%   List = [_|_],
   	increasing_list(List,Newhead,NewList2),

	one_element(Newhead,New),
    chop_up(NewList2, NewList).

%for single element list.
chop_up(List, [NewList3|NewList]) :-
%   List = [_|_],
   	increasing_list(List,Newhead,NewList2),
   
   	merge_list(Newhead,NewList3),  
    chop_up(NewList2, NewList).
 
%for the others.
chop_up(List, [Newhead|NewList]) :-
%   List = [Head|Tail],
   	increasing_list(List,Newhead,NewList2),
    chop_up(NewList2, NewList).


%part5 Tree_eval(Value, Tree, Eval)
		%tree(empty,Num,empty)
		%tree(empty,z,empty)

% tree(empty,z,empty)
tree_eval(Value, Tree, Eval) :-
    Tree = tree(empty, z, empty),
    Eval is Value.

% tree(empty,Num,empty)
tree_eval(_, Tree, Eval) :-
    Tree = tree(empty, Num, empty),
    Eval is Num.

% addition
tree_eval(Value, tree(L, +, R), Eval) :-
  tree_eval(Value, L, LEval),
  tree_eval(Value, R, REval),
  Eval is LEval + REval.
  
% subtraction
tree_eval(Value, tree(L, -, R), Eval) :-
  tree_eval(Value, L, LEval),
  tree_eval(Value, R, REval),
  Eval is LEval - REval.
  
% multiplication
tree_eval(Value, tree(L, *, R), Eval) :-
  tree_eval(Value, L, LEval),
  tree_eval(Value, R, REval),
  Eval is LEval * REval.
  
% division
tree_eval(Value, tree(L, /, R), Eval) :-
  tree_eval(Value, L, LEval),
  tree_eval(Value, R, REval),
  Eval is LEval / REval.

	

