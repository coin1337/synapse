#ifdef GL_ES
precision mediump float;
#endif

#extension GL_OES_standard_derivatives : enable

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

void main( void ) {
	float dist = .1;
	float radius = .2;

	vec3 col = vec3(0.01, -0.05, 0.05);
	vec2 centr = 2.0 * (gl_FragCoord.xy * 2.0 - resolution) /
		min(resolution.x, resolution.y);

	for(float i = 0.0; i < 50.0; i++)
	{
		float t1 = 1.0 + 0.01 * i;
		float t2 = 1.0 - 0.01 * i;
		float si = sin(time*t1 + i * dist) / 0.5;
		float co = cos(time*t2 + i * dist) * 0.5;
		float hue = i * 0.1 + time * 0.1;
		float third = 3.14159 / 1.5;

		vec3 Color = vec3(0.5 + 0.5 * sin(hue-third), 0.5 + 0.5 * sin(hue), 0.5 + 0.5 * sin(hue+third)) * 2.0 * (1.0+sin(time*t1*4.43534));

	  col += Color * 0.0032469 / abs(length(centr + vec2(si , co * si )) - radius);
	}


	gl_FragColor = vec4(col, 1.0);
}