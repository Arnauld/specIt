hljs.LANGUAGES.specit_en = function() {
  return {
    defaultMode: {
      contains: [
        {
          className: 'keyword',
          begin: '^\\s*(But |And |Then |When |Given |\\* |Narrative:|Scenario:|Background:|Feature|Require|Example:|Repeat)',
          relevance: 0
        },
        {
          className: 'localvars',
          begin: '\\[', end: '\\]',
          relevance: 0
        },
        {
          className: 'string',
          begin: '\\<', end: '\\>',
          relevance: 0
        },
        {
          className: 'localvars',
          begin: '^\\s*Fragment',
          end: '$',
          returnBegin: true,
          excludeBegin: true,
          contains: [
            {
                className: 'keyword',
                begin: '^\\s*Fragment',
                end:' ',
                relevance: 0
            }
          ],
          relevance: 10
        },
        {
          className: 'string',
          begin: '\\|',
          relevance: 0
        },
        hljs.HASH_COMMENT_MODE,
        hljs.C_LINE_COMMENT_MODE,
        hljs.C_BLOCK_COMMENT_MODE,
        {
          className: 'string',
          begin: '"""', end: '"""',
          relevance: 10
        },
        hljs.APOS_STRING_MODE,
        hljs.QUOTE_STRING_MODE,
        hljs.C_NUMBER_MODE,
        {
          className: 'annotation', begin: '@[^@\r\n\t ]+'
        }
      ]
    }
  };
}();