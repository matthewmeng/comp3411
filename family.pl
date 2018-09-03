% Program:  lists.pl
% Source:   Prolog
%
% Purpose:  This is a solution to the lists.pl exercises in the COMP9414/9814 lab.
%           See lab.html for a full description.
%
% History:  Original code by Barry Drake, adapted to SWI-Prolog by Bill Wilson


% []                   is the empty list 
% .(Head, Tail)        places one element on the front of the list
%                      Head is any element, Tail is the rest of the list.
%
is_a_list([]).
is_a_list(.(_Head, Tail)) :-
    is_a_list(Tail).

% head_tail(List, Head, Tail) extract the Head and Tail from the List
%
head_tail(.(Head, Tail), Head, Tail).

% is_member(Element, List)   is Element in the List?
%
is_member(Element, .(Element, _)).       % base case
is_member(Element, .(_, Tail)) :-        % recursive case
    is_member(Element, Tail).

% cons(List1, List2, Result)
%
% Result is the concatenation of List1 and List2
%
cons([], L, L).
cons([Head|Tail], List, [Head|TailResult]) :-
    cons(Tail, List, TailResult).


% listCount(List, Count)
% Count is bound to the number of elements in List.
%
listCount([], 0).
listCount([_|Tail], Count) :-
    listCount(Tail, TailCount),
    Count is TailCount + 1.


% deepListCount(NestedLists, Count)
% Count is bound to the number of elements in found in the nested lists,
% NestedLists.
%
% Some examples
%    : deepListCount([], Count)?
%    Count = 0
%    : deepListCount([a, b, c], Count)?
%    Count = 3
%    : deepListCount([[a, b, c]], Count)?
%    Count = 3
%    : deepListCount([a, [b, c], [[d], e], f], Count)?
%    Count = 6
%
deepListCount([],0).                     % base case

deepListCount(A, 1) :-
        A \= [], not(A = [_|_]).         % A is not a list

deepListCount([H|T], N) :-               % recursive case
        deepListCount(H, NH),
        deepListCount(T, NT),
        N is NH + NT.