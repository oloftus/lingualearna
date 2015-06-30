module.exports = function (grunt) {

    grunt.initConfig({
        bower: {
            dev: {
                rjsConfig: "src/javascript/config/require.js",
                options: {
                    baseUrl: "javascript"
                }
            },
            prod: {
                rjsConfig: "src/javascript/config/require.js",
                options: {
                    baseUrl: "javascript/config"
                }
            }
        },
        requirejs: {
            notebook: {
                options: {
                    baseUrl: "src/javascript",
                    mainConfigFile: "src/javascript/config/require.js",
                    include: ["notebook"],
                    insertRequire: ["notebook"],
                    out: "target/javascript/notebook.min.js",
                    optimize: "uglify2",
                    onModuleBundleComplete: function (data) {

                        var fs = require("fs");
                        var amdclean = require("amdclean");
                        var outputFile = data.path;

                        fs.writeFileSync(outputFile, amdclean.clean({
                            filePath: outputFile
                        }));
                    }
                }
            },
            reader: {
                options: {
                    baseUrl: "src/javascript",
                    mainConfigFile: "src/javascript/config/require.js",
                    include: ["reader"],
                    insertRequire: ["reader"],
                    out: "target/javascript/reader.min.js",
                    optimize: "uglify2",
                    onModuleBundleComplete: function (data) {

                        var fs = require("fs");
                        var amdclean = require("amdclean");
                        var outputFile = data.path;

                        fs.writeFileSync(outputFile, amdclean.clean({
                            filePath: outputFile
                        }));
                    }
                }
            }

        },
        targethtml: {
            prod: {
                files: [
                    {
                        cwd: "src/jsp/",
                        src: "**/*.{jsp,html}",
                        dest: "WEB-INF/jsp/",
                        expand: true
                    }
                ]
            },
            dev: {
                files: [
                    {
                        cwd: "src/jsp/",
                        src: "**/*.{jsp,html}",
                        dest: "WEB-INF/jsp/",
                        expand: true
                    }
                ]
            }
        },
        uglify: {
            options: {
                compress: {
                    global_defs: {
                        IN_PRODUCTION: true
                    }
                }
            },
            main: {
                files: {
                    "target/javascript/bootReader.min.js": "src/javascript/bootstrap/reader.js"
                }
            }
        },
        clean: ["WEB-INF/jsp", "target", "js", "resources", "templates"],
        compass: {
            dev: {
                options: {
                    watch: true,
                    sassDir: "src/scss",
                    cssDir: "target/resources/css",
                    outputStyle: "expanded"
                }
            },
            prod: {
                options: {
                    sassDir: "src/scss",
                    cssDir: "target/resources/css",
                    outputStyle: "compressed"
                }
            }
        }
    });

    grunt.loadNpmTasks("grunt-bower-requirejs");
    grunt.loadNpmTasks("grunt-contrib-requirejs");
    grunt.loadNpmTasks("grunt-targethtml");
    grunt.loadNpmTasks("grunt-contrib-clean");
    grunt.loadNpmTasks("grunt-contrib-compass");
    grunt.loadNpmTasks("grunt-contrib-uglify");

    grunt.registerTask("build-dev",
        [
            "bower:dev",
            "targethtml:dev",
            "compass:dev"
        ]);

    grunt.registerTask("build-prod",
        [
            "bower:prod",
            "targethtml:prod",
            "requirejs:notebook",
            "requirejs:reader",
            "uglify",
            "compass:prod"
        ]);
};
