#version 330 core

in vec2 vTexCoord;
in vec4 vColor;
in vec3 vNormal;

uniform sampler2D tex;

void main()
{
    gl_FragColor = vColor * texture(tex, vTexCoord);
}