public enum Type {//type of Node
	number,//number
	minus,//unary -
	add,
	subtract,
	multiply, 
	divide,
	bolean,//note: boolean is reserved word in java, so we define bolean
	not,
	or,
	and,
	equal,//==
	unequal,//!=
	gte,//greater than or equal
	lte,//less than or equal
	gt,//greater than
	lt,//less than
	identifier,//identifier of constant or function
	conditional_expression// conditional construct with "?" and ":"
}
