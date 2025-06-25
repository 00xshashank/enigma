package org.foxx.enigma;

import java.util.List;

abstract class Expr {
	interface Visitor<R> {
		R visitAssignmentExpr( Expr.Assignment expr );
		R visitBinaryExpr( Expr.Binary expr );
		R visitCallExpr( Expr.Call expr );
		R visitGroupingExpr( Expr.Grouping expr );
		R visitGetExpr( Expr.Get expr );
		R visitLiteralExpr( Expr.Literal expr );
		R visitLogicalExpr( Expr.Logical expr );
		R visitSetExpr( Expr.Set expr );
		R visitUnaryExpr( Expr.Unary expr );
		R visitVariableExpr( Expr.Variable expr );
	}
	class Assignment {
		Assignment (Token name, Expr expression) {
			this.name = name;
			this.expression = expression;
		}

			public final Token name;
			public final Expr expression;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitAssignmentExpr(this);
		}
	}
	class Binary {
		Binary (Expr left, Token operator, Expr right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

			public final Expr left;
			public final Token operator;
			public final Expr right;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitBinaryExpr(this);
		}
	}
	class Call {
		Call (Expr callee, Token paren, List<Expr> arguments) {
			this.callee = callee;
			this.paren = paren;
			this.arguments = arguments;
		}

			public final Expr callee;
			public final Token paren;
			public final List<Expr> arguments;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitCallExpr(this);
		}
	}
	class Grouping {
		Grouping (Expr expression) {
			this.expression = expression;
		}

			public final Expr expression;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitGroupingExpr(this);
		}
	}
	class Get {
		Get (Expr object, Token name) {
			this.object = object;
			this.name = name;
		}

			public final Expr object;
			public final Token name;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitGetExpr(this);
		}
	}
	class Literal {
		Literal (Object value) {
			this.value = value;
		}

			public final Object value;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitLiteralExpr(this);
		}
	}
	class Logical {
		Logical (Expr left, Token operator, Expr right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

			public final Expr left;
			public final Token operator;
			public final Expr right;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitLogicalExpr(this);
		}
	}
	class Set {
		Set (Expr object, Token name) {
			this.object = object;
			this.name = name;
		}

			public final Expr object;
			public final Token name;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitSetExpr(this);
		}
	}
	class Unary {
		Unary (Token operator, Expr right) {
			this.operator = operator;
			this.right = right;
		}

			public final Token operator;
			public final Expr right;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitUnaryExpr(this);
		}
	}
	class Variable {
		Variable (Token name) {
			this.name = name;
		}

			public final Token name;
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitVariableExpr(this);
		}
	}
	abstract <R> R accept(Visitor<R> visitor);
}
