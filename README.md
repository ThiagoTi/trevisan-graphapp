# trevisan-graphapp
App for drawing graphs and generating .svg and .pdf files of it. It has also a Fruchterman and Reingold algorithm for pleasant layout drawing for those who doesn't have a graph layout yet

###Examples

Fot input files, you'll need .txt files containing:

- Adjacency matrix:
```
0 1 0 0 0 1 0 0 0 0 0 0 0 1
1 0 1 0 0 0 0 0 0 0 1 0 0 0
0 1 0 1 0 0 0 1 0 0 0 0 0 0
0 0 1 0 1 0 0 0 0 0 0 0 1 0
0 0 0 1 0 1 0 0 0 1 0 0 0 0
1 0 0 0 1 0 1 0 0 0 0 0 0 0
0 0 0 0 0 1 0 1 0 0 0 1 0 0
0 0 1 0 0 0 1 0 1 0 0 0 0 0
0 0 0 0 0 0 0 1 0 1 0 0 0 1
0 0 0 0 1 0 0 0 1 0 1 0 0 0
0 1 0 0 0 0 0 0 0 1 0 1 0 0
0 0 0 0 0 0 1 0 0 0 1 0 1 0
0 0 0 1 0 0 0 0 0 0 0 1 0 1
1 0 0 0 0 0 0 0 1 0 0 0 1 0 
```

- Adjacency matrix, vertices with ID and positions fixed:
```
0 0 0 1 0 1 1 0 0 0
0 0 1 1 0 0 0 0 1 0
0 1 0 0 1 0 0 1 0 0
1 1 0 0 0 0 0 1 0 0
0 0 1 0 0 0 0 0 1 1
1 0 0 0 0 0 0 1 0 1
1 0 0 0 0 0 0 0 1 1
0 0 1 1 0 1 0 0 0 0
0 1 0 0 1 0 1 0 0 0
0 0 0 0 1 1 1 0 0 0
v0 213 750
v1 608 274
v2 464 153
v3 322 419
v4 595 405
v5 153 529
v6 533 741
v7 205 248
v8 742 518
v9 420 597
```

- Edge list:
```
8
0 1
0 2
0 3
1 4
1 5
2 4
2 7
3 5
3 7
4 6
5 6
6 7
```

-Edge list, vertices with ID and positions fixed:
```
8
0 1
0 2
0 3
1 4
1 5
2 4
2 7
3 5
3 7
4 6
5 6
6 7
v0 101 267
v1 263 244
v2 237 395
v3 221 121
v4 396 377
v5 380 101
v6 500 233
v7 348 253
```
