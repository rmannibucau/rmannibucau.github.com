(function () {
    var optionKeys = ['attributes'];
    var defaultOptions = ['showtitle'];
    var sections = document.querySelectorAll('[data-asciidoctor]');
    var attributes, options;
    for (var i = 0, len = sections.length; i < len; i++) {
        var currentAttr = sections[i].getAttribute('data-attributes');
        var currentOptions;
        if (!currentAttr) {
            if (!attributes) {
                attributes = sections[i].parentNode.getAttribute('data-attributes');
                options = attributes ? Opal.hash2(optionKeys, {attributes: attributes ? attributes.split(",") : defaultOptions}) : attributes;
            }
            currentAttr = attributes;
            currentOptions = options;
        } else {
            currentOptions = Opal.hash2(optionKeys, {attributes: currentAttr ? currentAttr.split(",") : defaultOptions});
        }
        var notes = sections[i].querySelector('aside.notes');
        sections[i].innerHTML = Opal.Asciidoctor.$convert(sections[i].textContent.trim().replace(/\r?\n */g, '\n'), currentOptions);
        if (notes) {
            sections[i].appendChild(notes);
        }
    }
})();
