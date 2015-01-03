'use strict';

// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// use this if you want to recursively match all sub-folders:
// 'test/spec/**/*.js'

// functions for grunt server
var mountFolder = function (connect, dir) {
    return connect.static(require('path').resolve(dir));
};
var proxySnippet = require('grunt-connect-proxy/lib/utils').proxyRequest;

// config array with default paths
var config = {

    app: 'src/main/webapp',
    dist: 'src/main/webapp/dist'

};

module.exports = function (grunt) {

    require('load-grunt-tasks')(grunt);
    require('time-grunt')(grunt);

    grunt.initConfig({

        config: config,

        // clean temp files and output directories
        clean: {
            dist: {
                files: [{
                    dot: true,
                    src: [
                        '.tmp',
                        '<%= config.dist %>'
                    ]
                }]
            },
            server: '.tmp'
        },

        // compile .less to .css
        less: {
            dist: {
                options: {
                    compress: true,
                    yuicompress: true,
                    optimization: 2,
                    sourceMap: false
                },
                files: {
                    '<%= config.app %>/styles/app.css': '<%= config.app %>/styles/app.less'
                }
            }
        },

        // validate files with JSHint - output is modified by jshint-stylish.
        jshint: {
            options: {
                jshintrc: '.jshintrc',
                reporter: require('jshint-stylish')
            },
            all: [
                'Gruntfile.js',
                '<%= config.app %>/app/**/*.js'
            ]
        },

        // useminPrepare task updates the grunt configuration to apply a configured transformation flow to tagged
        // files (i.e. blocks).
        useminPrepare: {
            src: '<%= config.app %>/index.html',
            options: {
                staging: '.tmp/',
                dest: '<%= config.dist %>'
            }
        },

        // The usemin task has 2 actions:
        //
        // - First it replaces all the blocks with a single "summary" line, pointing to a file creating by the
        //   transformation flow.
        // - Then it looks for references to assets (i.e. images, scripts, ...), and tries to replace them with their
        //   revved version if it can find one on disk
        usemin: {
            html: '<%= config.dist %>/**/*.html',
            css: '<%= config.dist %>/**/styles/*.css',
            options: {
                assetsDirs: [
                    '<%= config.dist %>',
                    '<%= config.dist %>/images'
                ]
            }
        },

        // minify images
        imagemin: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '<%= config.app %>/images',
                    src: '{,*/}*.{png,jpg,jpeg,gif}',
                    dest: '<%= config.dist %>/images'
                }]
            }
        },

        // minify html
        htmlmin: {
            dist: {
                files: [
                    {expand: true, cwd: '<%= config.app %>', src: ['*.html'], dest: '<%= config.dist %>'}
                ]
            }
        },

        // Put files not handled in other tasks here
        copy: {
            dist: {
                files: [{
                    expand: true,
                    dot: true,
                    cwd: '<%= config.app %>',
                    dest: '<%= config.dist %>',
                    src: [
                        '**/*.{ico,png,txt,eot,svg,ttf,woff}',
                        'WEB-INF/*'
                    ]
                }]
            },
            styles: {
                files: [
                    {expand: true, cwd: '<%= config.app %>/styles/', dest: '.tmp/concat/styles/', src: '*.css'}
                ]
            }
        },

        // run grunt tasks concurrently
        concurrent: {
            dist: [
                'less:dist',
                'copy:styles',
                'imagemin',
                'htmlmin'
            ],
            server: [
                'less:dist',
                'copy:styles'
            ]
        },

        // add, remove and rebuild AngularJS dependency injection annotations.
        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            dist: {
                files: [{
                    expand: true,
                    src: '.tmp/concat/app/*.js'
                }]
            }
        },

        // static asset revisioning through file content hash
        filerev: {
            dist: {
                src: [

                    '<%= config.dist %>/app/**/*.html',
                    '<%= config.dist %>/app/**/*.js',
                    '<%= config.dist %>/styles/*.css',
                    '<%= config.dist %>/images/*.{png,jpg,jpeg,gif,webp}'
                ]
            }
        },

        // run predefined tasks whenever watched file patterns are added, changed or deleted.
        watch: {
            styles: {
                files: ['src/main/webapp/app/styles/*.less'],
                tasks: ['less:dist', 'copy:styles'],
                options: {
                    nospawn: true
                }
            },
            livereload: {
                options: {
                    livereload: 35729
                },
                files: [
                    '<%= config.app %>/*.html',
                    '<%= config.app %>/app/**/*.html',
                    '<%= config.app %>/app/**/.js',
                    '<%= config.app %>/styles/*.css',
                    '<%= config.app %>/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}'
                ]
            }
        },

        // start a connect web server, includes proxy config for backend
        connect: {
            options: {
                port: 9000,
                // Change this to '0.0.0.0' to access the server from outside.
                hostname: 'localhost',
                livereload: 35729
            },
            proxies: {
                context: '/api',
                host: 'localhost',
                port: 8080,
                https: false,
                changeOrigin: false,
                xforward: true
            },
            livereload: {
                options: {
                    open: true,
                    base: [
                        '.tmp',
                        '<%= config.app %>'
                    ],
                    middleware: function (connect) {
                        return [
                            proxySnippet,
                            mountFolder(connect, '.tmp'),
                            mountFolder(connect, config.app)
                        ];
                    }
                }
            }
        }
    });

    grunt.registerTask('server', function () {

        grunt.task.run([
            'clean:server',
            'concurrent:server',
            'configureProxies',
            'connect:livereload',
            'watch'
        ]);
    });

    grunt.registerTask('build', [
        'clean:dist',
        'useminPrepare',
        'concurrent:dist',
        'concat:generated',
        'ngAnnotate',
        'copy:dist',
        'cssmin',
        'uglify:generated',
        'filerev',
        'usemin'
    ]);

    grunt.registerTask('default', [
        'jshint',
        'build'
    ]);
};