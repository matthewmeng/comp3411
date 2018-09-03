is_a_list([]).
is_a_list(.(H,T)):-
	is_a_list(T).