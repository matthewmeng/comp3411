test(f(A,B,C),D):-
	A=B,C=D.

head_next(L):-
	L = [H|T],
	T = [Next|_],
	Next is H +1.

put_in_tmpList(InputList,tmpList):-
	tmpList = [H|T],
	InputList = [IH|IT],
	IT = [Next,_],
	H > 0,
	T = Next.

put_in_tmpList(InputList,tmpList):-
	tmpList = [H|T],
	InputList = [IH|IT],
	IT = [Next,_],
	H = IH,
	T = Next.

get_the_increasing_list(List,tmpList,NewList):-
	List = [ListH|ListT],
	
	head_next(List),
	put_in_tmpList(List,tmpList)
	get_the_increasing_list(ListT,tmpList,NewList)

get_the_increasing_list(List,tmpList,NewList):-
	List = [ListH|ListT],

	not(head_next(List)),
	NewList(tmpList,NewList2)
	%get_the_increasing_list(ListT,tmpList,NewList)

chop_up([],NewList):-
	NewList = [].

chop_up([_],NewList):-
	NewList = [_].

chop_up(List,NewList):-
	list[ListT|ListH],
	tmpList = [H|T],
	H = 0,
	get_the_increasing_list(List,tmpList,NewList),
	chop_up(ListT,NewList).










	head_next(L):-
	L = [H|T],
	T = [Next|_],
	Next is H +1.

is_tmp_list_Null(TmpList):-
	TmpList = [].

put_tmp_list(List,TmpList):-
	is_tmp_list_Null(TemptList),
	List = [LH|LT],
	LT=[LTH|LTT],
	TmpList = [TH|TL],
	TH = LH,
	TL = LTH.

put_tmp_list(List,TmpList):-
	not(is_tmp_list_Null(TemptList)),
	List = [LH|LT],
	LT=[LTH|LTT],
	TmpList = [TH|TL],
	TL = LTH.

go_put_tmp_list(inputList,currentList,outputlist):-
	inputList = [inputListH|inputListT],
	put_tmp_list(inputList,currentList),
	head_next(inputList),
	go_put_tmp_list(inputListT|currentList).

go_put_tmp_list(inputList,currentList,outputlist):-
	outputlist = [outputlistH|outputlistT],
	inputList = [inputListH|inputListT],
	put_tmp_list(inputList,currentList),
	not(head_next(inputList)).
	outputlist = inputListT.



chop_up(List, NewList) :-
    List = [],
    NewList = List.

chop_up(List, NewList) :-
    List = [_],
    NewList = List.

chop_up(List, NewList) :-
    List = [Head|Tail],
   	not(head_next(List)),
    chop_up(Tail, NewList2),
    NewList = [Head|NewList2].

chop_up(List, NewList) :-
    List = [Head|Tail],
    NewList = [NewListHead|NewListTail],
    head_next(List),
    TemptList = [],

    go_put_tmp_list(List,TemptList,NewList3),

    chop_up(List, NewList2),
    NewList = [NewList3|NewList2].



tree_eval(_, empty, 0).
tree_eval(_, tree(empty), 0).

% Leaf case: z as value
tree_eval(Value, tree(empty, Num, empty), Eval) :-
  Num = z,
  Eval is Value.

% Leaf case: number (non z) as value
tree_eval(_, tree(empty, Num, empty), Eval) :-
  Num \= z,
  Eval is Num.

% Tree Case: for + operator
tree_eval(Value, tree(L, +, R), Eval) :-
  tree_eval(Value, L, LEval),
  tree_eval(Value, R, REval),
  Eval is LEval + REval.
  
% Tree Case: for - operator
tree_eval(Value, tree(L, -, R), Eval) :-
  tree_eval(Value, L, LEval),
  tree_eval(Value, R, REval),
  Eval is LEval - REval.
  
% Tree Case: for * operator
tree_eval(Value, tree(L, *, R), Eval) :-
  tree_eval(Value, L, LEval),
  tree_eval(Value, R, REval),
  Eval is LEval * REval.
  
% Tree Case: for / operator
tree_eval(Value, tree(L, /, R), Eval) :-
  tree_eval(Value, L, LEval),
  tree_eval(Value, R, REval),
  Eval is LEval / REval.