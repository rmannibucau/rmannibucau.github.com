all:
	asciidoctor -T ~/.asciidoctor/asciidoctor-backends/haml/deckjs -b html5 index.asciidoc --trace

clean:
	@if [ -f index.html ]; then rm index.html; fi
    
