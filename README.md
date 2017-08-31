# AF2ER

### Programa	 que	 recebe	 um	 Autômato	 Finito	 Não	Determinístico	com	transições	λ	(AFNλ) e	o	converte	em	uma	Expressão	Regular	(ER).

O	programa	deve	receber	uma	especificação	de	um	AFNλ	M = (E, Σ, δ, I, F) e	a	lista de	estados	R na	 ordem	 que	 devem	 ser	 removidos no	 formato	 JSON conforme	a	seguinte	especificação:
*{	"af":	[ [e,	∀e ∈	E], [a, ∀a ∈	Σ], [ [e,a,e’], δ(e, a) = e’], [i, ∀i ∈	I], [f, ∀f ∈	F] ],
 "r": [ r, ∀r ∈	R ] }*, por exemplo:

```sh
{ "af": [
 ["A", "B", "C", "D", "E"],
 ["0", "1"],
 [
 ["A", "0", "B"],
 ["B", "0", "B"],
 ["B", "1", "C"],
 ["C", "0", "B"],
 ["C", "1", "D"],
 ["D", "0", "B"],
 ["D", "1", "E"],
 ["E", "0", "B"],
 ["E", "1", "E"] 
 ],
 ["A"],
 ["E"]
 ],
 "r": ["A", "C", "D", "E", "B"]
}
```

Dado o autômato:

![automato](http://i.imgur.com/0n54qZv.png)

Obtem-se a expressão regular: 0(0+10+110+1111\*0)\*1111\*
