function im = pnmread( name )
%
%   im =  pnmread(name) - read a pnm file
%
%   name:       pnm file name
%
%   im:         returned image (uint8)
%
%   Note:   If the image being read pbm or pgm type, im contains a single
%           image of gray values, size m by n.  If the image is color then
%           pnmread returns a color image size m by n by 3.
%           

    [file, msg] = fopen( name, "rb", "native" );
    if ( file == -1 )
        error( sprintf( '%s <%s>', msg, name ) );
    end
    
    ch1 = fgets( file, 1 );
    if ( ch1 != 'P' )
        error( "invalid magic number" );
    end
    ch2 = fgets( file, 1 );
    if ( index("2356", ch2) == 0 )
        error( "invalid magic number" );
    end

    nc = pnm_getint( file );
    nr = pnm_getint( file );
    if ( nr <= 0 || nc <= 0 )
        error( "illegal image dimensions (must be positive)" );
    end
    max = pnm_getint( file );
    if ( max < 0 )
        error( "illegal max pixel value (must be non-negative)" );
    end
    
    if ( ch2 == '2' )
        % PGM ascii format
        im = zeros(nr, nc);
        for i = 1:nr
            for j = 1:nc
                im(i,j) = pnm_getint(file);
            end
        end
    elseif ( ch2 == '3' )
        % PPM ascii format
        im = zeros( [nr, nc, 3] );
        for i = 1:nr
            for j = 1:nc
                im(i,j, 1) = pnm_getint(file);
                im(i,j, 2) = pnm_getint(file);
                im(i,j, 3) = pnm_getint(file);
            end
        end
    elseif ( ch2 == '5' )
        % PGM raw format
        [im, n] = fread( file, [nc,nr], 'uint8=>uint8' );
        im = im';
        if n ~= (nr * nc)
            error( "no image data read" );
        end
    elseif ( ch2 == '6' )
        % PNM raw format
        [tmp, n] = fread( file, [3*nc,nr], 'uint8=>uint8' );
        tmp = tmp';
        if ( n ~= (3 * nr * nc) )
            error( "no image data read" );
        end
        im = zeros( [nr nc 3], 'uint8' );
        im(:,:,1) = tmp( :, 1:3:end );
        im(:,:,2) = tmp( :, 2:3:end );
        im(:,:,3) = tmp( :, 3:3:end );
    else
        error( 'invalid magic number' );
    end



    fclose(file);
end


function n = pnm_getint( file )
#
# PNM_GETINT(file) - read an integer from a pnm file
#
#    file : file id for open file
#

    # read past white space
    while ( 1 )
        ch = pnm_getc( file );
        if ( index( " \t\n\r", ch ) == 0 )
            break;
        endif
    end

    if ( ~isdigit(ch) )
        error( "pnm file format error" );
    end

    i = 0;
    while ( 1 )
        i = i * 10 + str2num(ch);
        ch = pnm_getc( file );
        if ( ~isdigit(ch) )
            break;
        end
    end

    n = i;
end


function c = pnm_getc( file )
#
# PNM_GETC(file) - read a character from a pnm file
#
#    file : file id (must be opened)
#
    ich = fgets( file, 1 );
    if ( !ischar(ich) )
        error( "unexpected end of file" );
    end
    
    ch = ich;
    
    if ( ch == '#' )
        while (1)
            ich = fgets( file, 1 );
            if ( !ischar(ich) )
                error( "unexpected end of file" );
            end
            ch = ich;
            if ( index( "\n\r", ch ) != 0 )
                break;
            end
        end
    end

    c = ch;
end

