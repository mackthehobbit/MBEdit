attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec2 vTexCoords;

uniform mat4 u_projTrans;

void main() {
	vTexCoords = a_texCoord0;
	gl_Position = u_projTrans * vec4(a_position.xyz, 1.0);
}