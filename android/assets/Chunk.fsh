varying vec2 vTexCoords;

uniform sampler2D u_texture;

void main() {
	vec4 color = texture2D(u_texture, vTexCoords);
	if(color.a < 0.5f)
		discard;
	gl_FragColor = color;
}