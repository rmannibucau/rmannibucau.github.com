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

        var content = sections[i].innerHTML;
        var whiteSpaces = 0;
        for (var skip = 0; skip < content.length; skip++) {
            var c = content.charAt(skip);
            if (c == '\n') {
                whiteSpaces = 0;
            } else if (c != ' ' && c != '\t') {
                break;
            } else {
                whiteSpaces++;
            }
        }

        sections[i].innerHTML = Opal.Asciidoctor.$convert(content.trim().replace(new RegExp('\r?\n {' + whiteSpaces + '}', 'g'), '\n'), currentOptions);
        if (notes) {
            sections[i].appendChild(notes);
        }
    }
})();
